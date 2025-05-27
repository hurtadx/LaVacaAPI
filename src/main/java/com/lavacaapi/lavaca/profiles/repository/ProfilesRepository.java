package com.lavacaapi.lavaca.profiles.repository;

import com.lavacaapi.lavaca.profiles.Profiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfilesRepository extends JpaRepository<Profiles, UUID> {

    // Búsquedas básicas
    Optional<Profiles> findByUserId(UUID userId);
    Optional<Profiles> findByEmail(String email);
    Optional<Profiles> findByUsername(String username);

    // Verificadores de existencia
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByUserId(UUID userId);

    // Búsquedas parciales
    List<Profiles> findByUsernameContainingIgnoreCase(String usernamePattern);
    List<Profiles> findByEmailContainingIgnoreCase(String emailPattern);

    // Búsquedas por fecha
    List<Profiles> findByCreatedAtAfter(Timestamp date);
    List<Profiles> findByCreatedAtBefore(Timestamp date);
    List<Profiles> findByUpdatedAtAfter(Timestamp date);

    // Perfiles más recientes
    List<Profiles> findTop10ByOrderByCreatedAtDesc();

    // Perfiles actualizados recientemente
    List<Profiles> findTop10ByOrderByUpdatedAtDesc();

    // Búsqueda de perfiles con correos de cierto dominio
    @Query("SELECT p FROM Profiles p WHERE p.email LIKE %:domain")
    List<Profiles> findByEmailDomain(@Param("domain") String domain);

    // Contar perfiles creados por periodo
    @Query("SELECT COUNT(p) FROM Profiles p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    Long countProfilesCreatedBetween(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    // Búsqueda por múltiples IDs de usuario
    List<Profiles> findByUserIdIn(List<UUID> userIds);

    // Eliminar perfil por ID de usuario
    void deleteByUserId(UUID userId);


    // Estos metodos sirven para:
    // Búsquedas por nombre de usuario o correo electrónico
    // Verificar existencia de perfiles
    // Filtrar perfiles por fecha de creación o actualización
    // Obtener perfiles más recientes o actualizados
    // Buscar perfiles por dominio de correo electrónico
    // Contar perfiles creados en un periodo específico
}

