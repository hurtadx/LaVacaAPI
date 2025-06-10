package com.lavacaapi.lavaca.vacas.service;

import com.lavacaapi.lavaca.participants.Participants;
import com.lavacaapi.lavaca.vacas.Vacas;
import com.lavacaapi.lavaca.vacas.repository.VacasRepository;
import com.lavacaapi.lavaca.participants.repository.ParticipantsRepository;
import com.lavacaapi.lavaca.transactions.repository.TransactionsRepository;
import com.lavacaapi.lavaca.events.repository.EventsRepository;
import com.lavacaapi.lavaca.rules.repository.RulesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.*;
import java.util.ArrayList;

@Service
public class VacasService {

    @Autowired
    private VacasRepository vacasRepository;

    @Autowired
    private ParticipantsRepository participantsRepository;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private RulesRepository rulesRepository;

    /**
     * Crea una nueva vaca
     * @param vaca datos de la vaca a crear
     * @return vaca creada
     */
    @Transactional
    public Vacas createVaca(Vacas vaca) {
        // Validar datos obligatorios
        if (vaca.getName() == null || vaca.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la vaca es obligatorio");
        }

        if (vaca.getGoal() == null || vaca.getGoal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La meta debe ser mayor a 0");
        }

        // Validar que el userId no sea nulo
        if (vaca.getUserId() == null) {
            throw new IllegalArgumentException("El ID del usuario creador es obligatorio");
        }

        // Generar ID si no se proporciona
        if (vaca.getId() == null) {
            vaca.setId(UUID.randomUUID());
        }

        // Establecer valores por defecto
        if (vaca.getCurrent() == null) {
            vaca.setCurrent(BigDecimal.ZERO);
        }

        if (vaca.getCreatedAt() == null) {
            vaca.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        if (vaca.getIsActive() == null) {
            vaca.setIsActive(true);
        }

        // Usar valores válidos para el status según la restricción de la base de datos
        // Solo se permiten: draft, open, closed, canceled (en minúsculas)
        if (vaca.getStatus() == null) {
            vaca.setStatus("draft");
        } else {
            String status = vaca.getStatus().toLowerCase();
            switch (status) {
                case "draft":
                case "open":
                case "closed":
                case "canceled":
                    vaca.setStatus(status);
                    break;
                case "active":
                    vaca.setStatus("open");
                    break;
                case "completed":
                    vaca.setStatus("closed");
                    break;
                case "cancelled":
                    vaca.setStatus("canceled");
                    break;
                default:
                    vaca.setStatus("draft"); // Valor por defecto si no coincide
            }
        }
        return vacasRepository.save(vaca);
    }

    /**
     * Obtiene todas las vacas
     * @return lista de vacas
     */
    public List<Vacas> getAllVacas() {
        return vacasRepository.findAll();
    }

    /**
     * Obtiene una vaca por su ID
     * @param id ID de la vaca
     * @return vaca encontrada o empty si no existe
     */
    public Optional<Vacas> getVacaById(UUID id) {
        return vacasRepository.findById(id);
    }

    /**
     * Obtiene vacas por su estado de activación
     * @param isActive estado de activación
     * @return lista de vacas con el estado especificado
     */
    public List<Vacas> getVacasByIsActive(Boolean isActive) {
        return vacasRepository.findByIsActive(isActive);
    }

    /**
     * Obtiene vacas por su estado
     * @param status estado (ACTIVE, COMPLETED, CANCELLED, etc.)
     * @return lista de vacas con el estado especificado
     */
    public List<Vacas> getVacasByStatus(String status) {
        return vacasRepository.findByStatus(status);
    }

    /**
     * Obtiene todas las vacas de un usuario específico
     * @param userId ID del usuario
     * @return lista de vacas del usuario
     */
    public List<Vacas> getVacasByUserId(UUID userId) {
        System.out.println("[DEBUG] getVacasByUserId - userId recibido: " + userId);
        List<Vacas> vacas = vacasRepository.findAll().stream()
                .filter(v -> userId.equals(v.getUserId()))
                .toList();
        System.out.println("[DEBUG] getVacasByUserId - vacas encontradas: " + vacas.size());
        return vacas;
    }

    /**
     * Obtiene vacas donde el usuario es owner o participante, sin duplicados
     * @param userId ID del usuario
     * @return lista de vacas
     */
    public List<Vacas> getVacasByUserParticipation(UUID userId) {
        // Vacas donde el usuario es owner
        List<Vacas> ownerVacas = vacasRepository.findAll().stream()
                .filter(v -> userId.equals(v.getUserId()))
                .toList();
        // Vacas donde el usuario es participante
        List<UUID> participantVacaIds = participantsRepository.findByUserId(userId).stream()
                .map(Participants::getVacaId)
                .distinct()
                .toList();

        List<Vacas> participantVacas = participantVacaIds.isEmpty()
                ? List.of()
                : vacasRepository.findAllById(participantVacaIds);

        // Unir ambas listas y eliminar duplicados
        Map<UUID, Vacas> vacaMap = new HashMap<>();
        ownerVacas.forEach(v -> vacaMap.put(v.getId(), v));
        participantVacas.forEach(v -> vacaMap.put(v.getId(), v));

        return new ArrayList<>(vacaMap.values());
    }

    /**
     * Actualiza una vaca existente
     * @param id ID de la vaca a actualizar
     * @param vaca Datos actualizados
     * @return vaca actualizada
     */
    @Transactional
    public Vacas updateVaca(UUID id, Vacas vaca) {
        Vacas existingVaca = vacasRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la vaca con ID: " + id));

        // No permitir cambiar el ID
        vaca.setId(id);

        // Validar que si se cambia el nombre, no exista otra vaca con ese nombre
        if (vaca.getName() != null && !vaca.getName().equals(existingVaca.getName()) &&
                vacasRepository.existsByNameIgnoreCase(vaca.getName())) {
            throw new IllegalArgumentException("Ya existe una vaca con ese nombre");
        }

        // Preservar valores que no deben cambiar directamente
        if (vaca.getCurrent() == null) {
            vaca.setCurrent(existingVaca.getCurrent());
        }

        if (vaca.getCreatedAt() == null) {
            vaca.setCreatedAt(existingVaca.getCreatedAt());
        }

        // Actualizar el estado si la meta ha sido alcanzada
        if (vaca.getCurrent().compareTo(vaca.getGoal()) >= 0) {
            vaca.setStatus("COMPLETED");
        }

        return vacasRepository.save(vaca);
    }

    /**
     * Elimina una vaca
     * @param id ID de la vaca a eliminar
     */
    @Transactional
    public void deleteVaca(UUID id) {
        if (!vacasRepository.existsById(id)) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + id);
        }

        // Alternativamente, podrías marcarla como inactiva en lugar de eliminarla
        // Vacas vaca = vacasRepository.findById(id).get();
        // vaca.setIsActive(false);
        // vaca.setStatus("DELETED");
        // vacasRepository.save(vaca);

        vacasRepository.deleteById(id);
    }

    /**
     * Resumen completo de una vaca: participantes, transacciones, progreso, reglas
     */
    public Map<String, Object> getVacaSummary(UUID vacaId) {
        Vacas vaca = vacasRepository.findById(vacaId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId));
        Map<String, Object> summary = new HashMap<>();
        summary.put("vaca", vaca);
        summary.put("participantes", participantsRepository.findByVacaId(vacaId));
        summary.put("transacciones", transactionsRepository.findByVacaId(vacaId));
        summary.put("eventos", eventsRepository.findByVacaIdOrderByCreatedAtDesc(vacaId));
        summary.put("reglas", rulesRepository.findByVacaId(vacaId));
        summary.put("progreso", getVacaProgress(vacaId));
        return summary;
    }

    /**
     * Progreso actual de una vaca hacia su meta
     */
    public Map<String, Object> getVacaProgress(UUID vacaId) {
        Vacas vaca = vacasRepository.findById(vacaId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId));
        Map<String, Object> progress = new HashMap<>();
        progress.put("current", vaca.getCurrent());
        progress.put("goal", vaca.getGoal());
        progress.put("percentage", vaca.getGoal().compareTo(BigDecimal.ZERO) > 0 ? vaca.getCurrent().multiply(BigDecimal.valueOf(100)).divide(vaca.getGoal(), 2, BigDecimal.ROUND_HALF_UP) : 0);
        return progress;
    }

    /**
     * Línea de tiempo de eventos de la vaca
     */
    public List<?> getVacaTimeline(UUID vacaId) {
        return eventsRepository.findByVacaIdOrderByCreatedAtDesc(vacaId);
    }

    /**
     * Permite a un usuario unirse a una vaca
     * @param vacaId ID de la vaca
     * @param userId ID del usuario que se une
     */
    @Transactional
    public void joinVaca(UUID vacaId, UUID userId) {
        // Verificar que la vaca exista
        Vacas vaca = vacasRepository.findById(vacaId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId));

        // Verificar que la vaca esté activa
        if (!vaca.getIsActive()) {
            throw new IllegalArgumentException("No es posible unirse a una vaca inactiva");
        }

        // Verificar si el usuario ya es participante de la vaca
        if (participantsRepository.existsByVacaIdAndUserId(vacaId, userId)) {
            throw new IllegalArgumentException("El usuario ya es participante de esta vaca");
        }

        // Crear un nuevo participante
        var participant = new com.lavacaapi.lavaca.participants.Participants();
        participant.setId(UUID.randomUUID());
        participant.setVacaId(vacaId);
        participant.setUserId(userId);
        participant.setStatus("activo");
        participant.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        participantsRepository.save(participant);

        // Registrar el evento
        var event = new com.lavacaapi.lavaca.events.Events();
        event.setId(UUID.randomUUID());
        event.setVacaId(vacaId);
        event.setUserId(userId);
        event.setTitle("Nuevo participante");
        event.setDescription("Se ha unido un nuevo participante a la vaca");
        event.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        eventsRepository.save(event);
    }

    /**
     * Permite a un usuario salir de una vaca
     * @param vacaId ID de la vaca
     * @param userId ID del usuario que sale
     */
    @Transactional
    public void leaveVaca(UUID vacaId, UUID userId) {
        // Verificar que la vaca exista
        if (!vacasRepository.existsById(vacaId)) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId);
        }

        // Verificar que el usuario sea participante de la vaca
        var participant = participantsRepository.findByVacaIdAndUserId(vacaId, userId)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no es participante de esta vaca"));

        // No permitir salir si es el creador de la vaca
        Vacas vaca = vacasRepository.findById(vacaId).get();
        if (vaca.getUserId().equals(userId)) {
            throw new IllegalArgumentException("El creador de la vaca no puede salir de ella");
        }

        // Eliminar al participante
        participantsRepository.delete(participant);

        // Registrar el evento
        var event = new com.lavacaapi.lavaca.events.Events();
        event.setId(UUID.randomUUID());
        event.setVacaId(vacaId);
        event.setUserId(userId);
        event.setTitle("Participante retirado");
        event.setDescription("Un participante ha salido de la vaca");
        event.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        eventsRepository.save(event);
    }

    /**
     * Cambia el estado de una vaca
     * @param vacaId ID de la vaca
     * @param status nuevo estado (ACTIVE, PAUSED, COMPLETED, CANCELLED)
     */
    @Transactional
    public void updateVacaStatus(UUID vacaId, String status) {
        // Validar el estado
        if (status == null || (!status.equals("ACTIVE") && !status.equals("PAUSED") &&
                !status.equals("COMPLETED") && !status.equals("CANCELLED"))) {
            throw new IllegalArgumentException("El estado debe ser ACTIVE, PAUSED, COMPLETED o CANCELLED");
        }

        // Verificar que la vaca exista
        Vacas vaca = vacasRepository.findById(vacaId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId));

        // Actualizar el estado
        vaca.setStatus(status);

        // Si se completa o cancela, desactivarla
        if (status.equals("COMPLETED") || status.equals("CANCELLED")) {
            vaca.setIsActive(false);
        } else {
            vaca.setIsActive(true);
        }

        vacasRepository.save(vaca);

        // Registrar el evento
        var event = new com.lavacaapi.lavaca.events.Events();
        event.setId(UUID.randomUUID());
        event.setVacaId(vacaId);
        event.setUserId(vaca.getUserId());
        event.setTitle("Cambio de estado");
        event.setDescription("El estado de la vaca ha cambiado a: " + status);
        event.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        eventsRepository.save(event);
    }
}

