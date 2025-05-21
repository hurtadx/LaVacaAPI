package com.lavaca.lavacaapi.repository;

import com.lavaca.lavacaapi.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    // Búsquedas básicas
    Optional<Profile> findByUserId(UUID userId);
    Optional<Profile> findByEmail(String email);
    Optional<Profile> findByUsername(String username);

    // Verificadores de existencia
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByUserId(UUID userId);

    // Búsquedas parciales
    List<Profile> findByUsernameContainingIgnoreCase(String usernamePattern);
    List<Profile> findByEmailContainingIgnoreCase(String emailPattern);

    // Búsquedas por fecha
    List<Profile> findByCreatedAtAfter(Timestamp date);
    List<Profile> findByCreatedAtBefore(Timestamp date);
    List<Profile> findByUpdatedAtAfter(Timestamp date);

    // Perfiles más recientes
    List<Profile> findTop10ByOrderByCreatedAtDesc();

    // Perfiles actualizados recientemente
    List<Profile> findTop10ByOrderByUpdatedAtDesc();

    // Búsqueda de perfiles con correos de cierto dominio
    @Query("SELECT p FROM Profile p WHERE p.email LIKE %:domain")
    List<Profile> findByEmailDomain(@Param("domain") String domain);

    // Contar perfiles creados por periodo
    @Query("SELECT COUNT(p) FROM Profile p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    Long countProfilesCreatedBetween(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    // Búsqueda por múltiples IDs de usuario
    List<Profile> findByUserIdIn(List<UUID> userIds);

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