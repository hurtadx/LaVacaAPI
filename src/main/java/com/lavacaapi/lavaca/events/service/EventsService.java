package com.lavacaapi.lavaca.events.service;

import com.lavacaapi.lavaca.events.Events;
import com.lavacaapi.lavaca.events.repository.EventsRepository;
import com.lavacaapi.lavaca.vacas.repository.VacasRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventsService {

    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private VacasRepository vacasRepository;

    /**
     * Crea un nuevo evento
     * @param event datos del evento a crear
     * @return evento creado
     */
    @Transactional
    public Events createEvent(Events event) {
        // Validar datos obligatorios
        if (event.getVacaId() == null) {
            throw new IllegalArgumentException("El ID de la vaca es obligatorio");
        }

        if (event.getTitle() == null || event.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del evento es obligatorio");
        }

        // Verificar que la vaca exista
        if (!vacasRepository.existsById(event.getVacaId())) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + event.getVacaId());
        }

        // Generar ID si no se proporciona
        if (event.getId() == null) {
            event.setId(UUID.randomUUID());
        }

        // Establecer fecha de creación si no se proporciona
        if (event.getCreatedAt() == null) {
            event.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        return eventsRepository.save(event);
    }

    /**
     * Obtiene todos los eventos
     * @return lista de eventos
     */
    public List<Events> getAllEvents() {
        return eventsRepository.findAll();
    }

    /**
     * Obtiene un evento por su ID
     * @param id ID del evento
     * @return evento encontrado o empty si no existe
     */
    public Optional<Events> getEventById(UUID id) {
        return eventsRepository.findById(id);
    }

    /**
     * Obtiene todos los eventos relacionados con una vaca
     * @param vacaId ID de la vaca
     * @return lista de eventos de la vaca
     */
    public List<Events> getEventsByVacaId(UUID vacaId) {
        if (!vacasRepository.existsById(vacaId)) {
            throw new EntityNotFoundException("No se encontró la vaca con ID: " + vacaId);
        }
        return eventsRepository.findByVacaId(vacaId);
    }

    /**
     * Obtiene todos los eventos de un usuario
     * @param userId ID del usuario
     * @return lista de eventos del usuario
     */
    public List<Events> getEventsByUserId(UUID userId) {
        return eventsRepository.findByUserId(userId);
    }

    /**
     * Obtiene los eventos más recientes
     * @param limit número máximo de eventos a obtener
     * @return lista de eventos recientes
     */
    public List<Events> getRecentEvents(int limit) {
        List<Events> events = eventsRepository.findTop10ByOrderByCreatedAtDesc();
        if (limit > 0 && limit < events.size()) {
            return events.subList(0, limit);
        }
        return events;
    }

    /**
     * Busca eventos por texto en título o descripción
     * @param query texto a buscar
     * @return lista de eventos que coinciden con la búsqueda
     */
    public List<Events> searchEvents(String query) {
        if (query == null || query.trim().isEmpty()) {
            return List.of();
        }

        List<Events> byTitle = eventsRepository.findByTitleContainingIgnoreCase(query);
        List<Events> byDescription = eventsRepository.findByDescriptionContainingIgnoreCase(query);

        // Combinar resultados y eliminar duplicados
        return byTitle.stream()
                .filter(event -> byDescription.stream()
                        .noneMatch(e -> e.getId().equals(event.getId())))
                .collect(Collectors.toCollection(() -> byDescription));
    }

    /**
     * Actualiza un evento existente
     * @param id ID del evento a actualizar
     * @param event Datos actualizados
     * @return evento actualizado
     */
    @Transactional
    public Events updateEvent(UUID id, Events event) {
        Events existingEvent = eventsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el evento con ID: " + id));

        // No permitir cambiar el ID ni la vaca
        event.setId(id);
        event.setVacaId(existingEvent.getVacaId());

        // Preservar fecha de creación
        if (event.getCreatedAt() == null) {
            event.setCreatedAt(existingEvent.getCreatedAt());
        }

        return eventsRepository.save(event);
    }

    /**
     * Elimina un evento
     * @param id ID del evento a eliminar
     */
    @Transactional
    public void deleteEvent(UUID id) {
        if (!eventsRepository.existsById(id)) {
            throw new EntityNotFoundException("No se encontró el evento con ID: " + id);
        }
        eventsRepository.deleteById(id);
    }
}
