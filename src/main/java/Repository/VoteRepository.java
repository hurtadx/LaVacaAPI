package Repository;

import Model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.security.Timestamp;
import java.util.List;
import java.util.UUID;

public interface VoteRepository extends JpaRepository<Vote, UUID> {

    // Buscar votos por vaca_id
    List<Vote> findByVaca_id(UUID vacaId);

    // Buscar votos por user_id
    List<Vote> findByUser_id(UUID userId);

    // Buscar votos por transaction_id
    List<Vote> findByTransaction_id(UUID transactionId);

    // Buscar por tipo de voto (approve/reject)
    List<Vote> findByVote(String vote);

    // Buscar votos combinando vaca_id y tipo de voto
    List<Vote> findByVaca_idAndVote(UUID vacaId, String vote);

    // Buscar votos de un usuario en una vaca específica
    List<Vote> findByUser_idAndVaca_id(UUID userId, UUID vacaId);

    // Buscar por fecha de creación
    List<Vote> findByCreated_atAfter(Timestamp date);
    List<Vote> findByCreated_atBefore(Timestamp date);

    // Verificar si un usuario ya ha votado en una transacción
    boolean existsByUser_idAndTransaction_id(UUID userId, UUID transactionId);

    // Contar votos por tipo en una transacción
    @Query("SELECT COUNT(v) FROM Vote v WHERE v.transaction_id = :transactionId AND v.vote = :voteType")
    Long countVotesByTransactionAndType(@Param("transactionId") UUID transactionId, @Param("voteType") String voteType);

    // Obtener los votos más recientes para una transacción
    List<Vote> findByTransaction_idOrderByCreated_atDesc(UUID transactionId);

    // Obtener el conteo de votos por tipo para una vaca
    @Query("SELECT v.vote, COUNT(v) FROM Vote v WHERE v.vaca_id = :vacaId GROUP BY v.vote")
    List<Object[]> countVotesByTypeForVaca(@Param("vacaId") UUID vacaId);

    // Buscar votos que contienen cierta razón
    List<Vote> findByReasonContainingIgnoreCase(String reasonText);

    // Encontrar los últimos N votos de un usuario
    List<Vote> findTop10ByUser_idOrderByCreated_atDesc(UUID userId);

    // estos metodos pueden servir para:
    // Obtener todos los votos para una transacción o vaca
    //Verificar si un usuario ya ha votado en una decisión
    //Contar votos a favor y en contra
    //Obtener estadísticas de votación
    //Buscar el historial de votación de un usuario
}