package Service;

public class VacaService {
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
        // El método findByUserId no existe en VacaRepository
        // Se necesita implementar una consulta personalizada o usar ParticipantRepository
        // para encontrar las vacas en las que participa el usuario

        // Ejemplo de implementación:
        // List<Participant> participants = participantRepository.findByUser_id(userId.toString());
        // return participants.stream()
        //     .map(p -> vacaRepository.findById(UUID.fromString(p.getVaca_id())))
        //     .filter(Optional::isPresent)
        //     .map(Optional::get)
        //     .collect(Collectors.toList());

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

    // Método para enviar emails (necesita implementación)
    private void sendEmail(String email, String subject, String body) {
        // Implementemos la lógica de envío de correos
        // Podemos usar JavaMailSender u otro servicio
    }

    // Acepta una invitación
    public void acceptInvitation(UUID invitationId, UUID userId) {
        // El objeto Participant necesita ser definido correctamente según el modelo
        Participant participant = new Participant();
        // Asumiendo que Participant tiene los métodos necesarios
        // participant.setId(UUID.randomUUID().toString());
        // participant.setVaca_id(invitationId.toString());
        // participant.setUser_id(userId.toString());
        // participant.setStatus("ACTIVE");

        // participantRepository.save(participant);
    }

    // Usuario abandona una vaca
    public void leaveVaca(UUID vacaId, UUID userId) {
        // El método findByVacaIdAndUserId no existe en ParticipantRepository
        // Debe usar el método correspondiente según la definición del repositorio
        // Por ejemplo: findByVaca_idAndUser_id

        // Participant participant = participantRepository.findByVaca_idAndUser_id(
        //    vacaId.toString(), userId.toString()).orElse(null);
        // if (participant != null) {
        //    participant.setStatus("INACTIVE");
        //    // Participant probablemente no tiene updatedAt
        //    participantRepository.save(participant);
        // }
    }

    // Obtiene participantes activos
    public List<Participant> getActiveParticipants(UUID vacaId) {
        // El método findByVacaIdAndStatus no existe en ParticipantRepository
        // Debe usar el método correspondiente según el repositorio
        // Por ejemplo: findByVaca_idAndStatus

        // return participantRepository.findByVaca_idAndStatus(vacaId.toString(), "ACTIVE");
        throw new UnsupportedOperationException("Método no implementado correctamente");
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

        if (userId != null) {
            participant.setUser_id(userId.toString());
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
        List<Participant> participant = participantRepository.findByUser_id(userId.toString());
        if (participant.isEmpty()) {
            return List.of();
        }

        List<String> vacaIds = participantRepository.findByUser_id(userId.toString())
            .stream()
            .map(Participant::getVaca_id)
            .toList();

        return vacaRepository.findAllById(vacaIds.stream()
            .map(UUID::fromString)
            .toList());
    }


}