package Service;

import Model.Vaca;
import Model.Participant;
import Repository.VacaRepository;
import Repository.ParticipantRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VacaService {

    @Autowired
    private VacaRepository vacaRepository;

    @Autowired
    private ParticipantRepository participantRepository;

    // DTO para recibir datos del frontend
    public static class VacaDTO {
        private String name;
        private String description;
        private BigDecimal goal;
        private String color;
        private Timestamp deadline;

        // Getters y setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public BigDecimal getGoal() { return goal; }
        public void setGoal(BigDecimal goal) { this.goal = goal; }

        public String getColor() { return color; }
        public void setColor(String color) { this.color = color; }

        public Timestamp getDeadline() { return deadline; }
        public void setDeadline(Timestamp deadline) { this.deadline = deadline; }
    }

    // Método para crear una vaca con datos del frontend
    public Vaca createVaca(VacaDTO vacaDTO) {
        Vaca vaca = new Vaca();
        vaca.setId(UUID.randomUUID());
        vaca.setName(vacaDTO.getName());
        vaca.setDescription(vacaDTO.getDescription());
        vaca.setGoal(vacaDTO.getGoal());
        vaca.setCurrent(new BigDecimal("0.00")); //se setea a 0 pq apenas se creo
        vaca.setColor(vacaDTO.getColor());
        vaca.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        vaca.setIsActive(true);
        vaca.setStatus("ACTIVE");

        if (vacaDTO.getDeadline() != null) {
            vaca.setDeadline(vacaDTO.getDeadline());
        }

        return vacaRepository.save(vaca);
    }

    // Método para obtener vacas por usuario (este método necesita implementación)
    public List<Vaca> getAllVacas(UUID userId) {
        throw new UnsupportedOperationException("Método no implementado correctamente");
    }

    // Busca una vaca por su id
    public Vaca getVacaById(UUID vacaId) {
        return vacaRepository.findById(vacaId)
                .orElseThrow(() -> new EntityNotFoundException("Vaca no encontrada"));
    }

    // Actualiza una vaca
    public Vaca updateVaca(UUID vacaId, Vaca updatedVaca) {
        Vaca existingVaca = getVacaById(vacaId);
        existingVaca.setName(updatedVaca.getName());
        existingVaca.setDescription(updatedVaca.getDescription());
        existingVaca.setGoal(updatedVaca.getGoal());
        existingVaca.setCurrent(updatedVaca.getCurrent());
        existingVaca.setColor(updatedVaca.getColor());
        // existingVaca.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return vacaRepository.save(existingVaca);
    }

    // Marca como inactiva una vaca (no la elimina)
    public void deactivateVaca(UUID vacaId) {
        Vaca vaca = getVacaById(vacaId);
        vaca.setIsActive(false);
        // vaca.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        vacaRepository.save(vaca);
    }

    // Genera una invitación
    public void generateInvitation(UUID vacaId, String email) {
        Vaca vaca = getVacaById(vacaId);
        String invitationLink = "https://example.com/invite/" + vaca.getId();
        String subject = "Invitación a la Vaca";
        String body = "Hola, has sido invitado a unirte a la vaca: " + vaca.getName() +
                      ". Haz clic en el siguiente enlace para unirte: " + invitationLink;

        // Método de envío de email (necesita implementación)
        sendEmail(email, subject, body);
    }

    // Implementación del método para enviar emails
    private void sendEmail(String email, String subject, String body) {
        // Aquí podrías usar JavaMailSender de Spring
        System.out.println("Enviando email a: " + email);
        System.out.println("Asunto: " + subject);
        System.out.println("Contenido: " + body);
        // Nota: Esta es una implementación de simulación.
        // En producción, debemos inyectar y usar un servicio de email real.
    }

    // Corrección del método acceptInvitation
    public void acceptInvitation(UUID vacaId, UUID userId) {
        Vaca vaca = getVacaById(vacaId);
        if (!vaca.getIsActive()) {
            throw new IllegalStateException("No se puede unir a una vaca inactiva");
        }

        if (participantRepository.existsByVaca_idAndUser_id(vacaId.toString(), userId.toString())) {
            throw new IllegalStateException("Ya eres participante en esta vaca");
        }

        Participant participant = new Participant();
        participant.setId(UUID.randomUUID().toString());
        participant.setVaca_id(vacaId.toString());
        participant.setUser_id(userId.toString());
        participant.setCreated_at(new Timestamp(System.currentTimeMillis()));
        participant.setStatus("activo"); // Ahora se asigna el status correctamente

        participantRepository.save(participant);
    }

    // Corrección del método leaveVaca
    public void leaveVaca(UUID vacaId, UUID userId) {
        Optional<Participant> participantOpt = participantRepository.findByVaca_idAndUser_id(
            vacaId.toString(), userId.toString());

        if (participantOpt.isEmpty()) {
            throw new EntityNotFoundException("No eres participante en esta vaca");
        }

        participantRepository.delete(participantOpt.get());
    }

    // Implementación del método getActiveParticipants
    public List<Participant> getActiveParticipants(UUID vacaId) {
        return participantRepository.findByVaca_idAndStatus(vacaId.toString(), "activo");
    }

    // Obtener todas las vacas activas
    public List<Vaca> getAllActiveVacas() {
        return vacaRepository.findByIsActive(true);
    }

    // Buscar vacas por nombre o parte del nombre
    public List<Vaca> searchVacasByName(String namePattern) {
        return vacaRepository.findByNameContainingIgnoreCase(namePattern);
    }

    // Obtener vacas próximas a vencer
    public List<Vaca> getUpcomingDeadlineVacas(int daysThreshold) {
        Timestamp thresholdDate = new Timestamp(System.currentTimeMillis() + daysThreshold * 86400000L);
        return vacaRepository.findByDeadlineBeforeAndIsActiveTrue(thresholdDate);
    }

    // Obtener vacas que han alcanzado un porcentaje de la meta
    public List<Vaca> getVacasNearingGoal(BigDecimal percentage) {
        return vacaRepository.findVacasNearingGoal(percentage);
    }

    // Método para añadir un participante a una vaca
    public Participant addParticipant(UUID vacaId, String email, String name, UUID userId) {
        Vaca vaca = getVacaById(vacaId);

        if (!vaca.getIsActive()) {
            throw new IllegalStateException("No se puede añadir participante a una vaca inactiva");
        }

        // Verificar si el participante ya existe
        if (participantRepository.existsByVaca_idAndEmail(vacaId.toString(), email)) {
            throw new IllegalStateException("El participante ya existe en esta vaca");
        }

        Participant participant = new Participant();
        participant.setId(UUID.randomUUID().toString());
        participant.setVaca_id(vacaId.toString());
        participant.setEmail(email);
        participant.setName(name);
        participant.setCreated_at(new Timestamp(System.currentTimeMillis()));
        participant.setStatus("pendiente"); // Por defecto, status pendiente

        if (userId != null) {
            participant.setUser_id(userId.toString());
            participant.setStatus("activo"); // Si tiene userId, status activo
        }

        return participantRepository.save(participant);
    }

    // Método para actualizar el monto actual de una vaca
    public Vaca updateVacaAmount(UUID vacaId, BigDecimal amount) {
        Vaca vaca = getVacaById(vacaId);
        vaca.setCurrent(vaca.getCurrent().add(amount));

        // Verificar si se ha alcanzado la meta
        if (vaca.getCurrent().compareTo(vaca.getGoal()) >= 0) {
            vaca.setStatus("COMPLETED");
        }

        return vacaRepository.save(vaca);
    }

    // Método para obtener las vacas más recientes
    public List<Vaca> getRecentVacas() {
        return vacaRepository.findTop5ByOrderByCreatedAtDesc();
    }

    // Método para contar participantes en una vaca
    public long countParticipantsInVaca(UUID vacaId) {
        return participantRepository.countParticipantsByVaca(vacaId.toString());
    }

    // Método para eliminar un participante
    public void removeParticipant(UUID vacaId, UUID userId) {
        participantRepository.deleteByVaca_idAndUser_id(vacaId.toString(), userId.toString());
    }

    // Método para obtener todas las vacas en las que participa un usuario
    public List<Vaca> getVacasByParticipant(UUID userId) {
        List<Participant> participants = participantRepository.findByUser_id(userId.toString());
        if (participants.isEmpty()) {
            return List.of();
        }

        List<UUID> vacaIds = participants.stream()
            .map(p -> UUID.fromString(p.getVaca_id()))
            .toList();

        return vacaRepository.findAllById(vacaIds);
    }
}

