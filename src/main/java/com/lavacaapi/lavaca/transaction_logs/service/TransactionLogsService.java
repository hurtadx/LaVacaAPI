package com.lavacaapi.lavaca.transaction_logs.service;

import com.lavacaapi.lavaca.transaction_logs.TransactionLogs;
import com.lavacaapi.lavaca.transaction_logs.repository.TransactionLogsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionLogsService {

    @Autowired
    private TransactionLogsRepository transactionLogsRepository;

    /**
     * Obtiene todos los logs de transacciones
     * @return lista de logs
     */
    public List<TransactionLogs> getAllTransactionLogs() {
        return transactionLogsRepository.findAll();
    }

    /**
     * Obtiene logs de transacciones paginados
     * @param pageable información de paginación
     * @return página de logs
     */
    public Page<TransactionLogs> getAllTransactionLogsPaginated(Pageable pageable) {
        return transactionLogsRepository.findAll(pageable);
    }

    /**
     * Obtiene un log por su ID
     * @param id ID del log
     * @return log encontrado o empty si no existe
     */
    public Optional<TransactionLogs> getTransactionLogById(UUID id) {
        return transactionLogsRepository.findById(id);
    }

    /**
     * Obtiene todos los logs relacionados con una transacción
     * @param transactionId ID de la transacción
     * @return lista de logs de la transacción
     */
    public List<TransactionLogs> getTransactionLogsByTransactionId(UUID transactionId) {
        return transactionLogsRepository.findByTransactionId(transactionId);
    }

    /**
     * Obtiene todos los logs generados por un usuario
     * @param userId ID del usuario
     * @return lista de logs del usuario
     */
    public List<TransactionLogs> getTransactionLogsByUserId(UUID userId) {
        return transactionLogsRepository.findByUserId(userId);
    }

    /**
     * Obtiene todos los logs de un tipo de acción específico
     * @param action tipo de acción (CREATE, UPDATE, DELETE, etc.)
     * @return lista de logs con la acción especificada
     */
    public List<TransactionLogs> getTransactionLogsByAction(String action) {
        return transactionLogsRepository.findByAction(action);
    }

    /**
     * Obtiene logs creados después de una fecha específica
     * @param timestamp fecha a partir de la cual buscar logs
     * @return lista de logs posteriores a la fecha especificada
     */
    public List<TransactionLogs> getTransactionLogsAfterTimestamp(Timestamp timestamp) {
        return transactionLogsRepository.findByTimestampAfter(timestamp);
    }

    /**
     * Crea un nuevo log de transacción
     * @param transactionLog datos del log
     * @return log creado
     */
    @Transactional
    public TransactionLogs createTransactionLog(TransactionLogs transactionLog) {
        // Validar datos obligatorios
        if (transactionLog.getTransactionId() == null) {
            throw new IllegalArgumentException("El ID de la transacción es obligatorio");
        }

        if (transactionLog.getUserId() == null) {
            throw new IllegalArgumentException("El ID del usuario es obligatorio");
        }

        if (transactionLog.getAction() == null || transactionLog.getAction().trim().isEmpty()) {
            throw new IllegalArgumentException("La acción es obligatoria");
        }

        // Generar ID si no se proporciona
        if (transactionLog.getId() == null) {
            transactionLog.setId(UUID.randomUUID());
        }

        // Establecer fecha si no se proporciona
        if (transactionLog.getTimestamp() == null) {
            transactionLog.setTimestamp(new Timestamp(System.currentTimeMillis()));
        }

        return transactionLogsRepository.save(transactionLog);
    }

    /**
     * Crea un log para una operación de creación
     * @param transactionId ID de la transacción
     * @param userId ID del usuario que realiza la acción
     * @param newData datos de la nueva transacción (como JSON)
     * @return log creado
     */
    @Transactional
    public TransactionLogs logCreate(UUID transactionId, UUID userId, String newData) {
        TransactionLogs log = new TransactionLogs();
        log.setId(UUID.randomUUID());
        log.setTransactionId(transactionId);
        log.setUserId(userId);
        log.setAction("CREATE");
        log.setOld_data(null);
        log.setNew_data(newData);
        log.setTimestamp(new Timestamp(System.currentTimeMillis()));

        return transactionLogsRepository.save(log);
    }

    /**
     * Crea un log para una operación de actualización
     * @param transactionId ID de la transacción
     * @param userId ID del usuario que realiza la acción
     * @param oldData datos anteriores de la transacción (como JSON)
     * @param newData datos nuevos de la transacción (como JSON)
     * @return log creado
     */
    @Transactional
    public TransactionLogs logUpdate(UUID transactionId, UUID userId, String oldData, String newData) {
        TransactionLogs log = new TransactionLogs();
        log.setId(UUID.randomUUID());
        log.setTransactionId(transactionId);
        log.setUserId(userId);
        log.setAction("UPDATE");
        log.setOld_data(oldData);
        log.setNew_data(newData);
        log.setTimestamp(new Timestamp(System.currentTimeMillis()));

        return transactionLogsRepository.save(log);
    }

    /**
     * Crea un log para una operación de eliminación
     * @param transactionId ID de la transacción
     * @param userId ID del usuario que realiza la acción
     * @param oldData datos de la transacción eliminada (como JSON)
     * @return log creado
     */
    @Transactional
    public TransactionLogs logDelete(UUID transactionId, UUID userId, String oldData) {
        TransactionLogs log = new TransactionLogs();
        log.setId(UUID.randomUUID());
        log.setTransactionId(transactionId);
        log.setUserId(userId);
        log.setAction("DELETE");
        log.setOld_data(oldData);
        log.setNew_data(null);
        log.setTimestamp(new Timestamp(System.currentTimeMillis()));

        return transactionLogsRepository.save(log);
    }

    /**
     * Crea un log para una operación de aprobación
     * @param transactionId ID de la transacción
     * @param userId ID del usuario que realiza la acción
     * @param oldData datos anteriores (estado de aprobación anterior)
     * @param newData datos nuevos (nuevo estado de aprobación)
     * @return log creado
     */
    @Transactional
    public TransactionLogs logApproval(UUID transactionId, UUID userId, String oldData, String newData) {
        TransactionLogs log = new TransactionLogs();
        log.setId(UUID.randomUUID());
        log.setTransactionId(transactionId);
        log.setUserId(userId);
        log.setAction("APPROVE");
        log.setOld_data(oldData);
        log.setNew_data(newData);
        log.setTimestamp(new Timestamp(System.currentTimeMillis()));

        return transactionLogsRepository.save(log);
    }

    /**
     * Elimina un log de transacción
     * @param id ID del log a eliminar
     */
    @Transactional
    public void deleteTransactionLog(UUID id) {
        if (!transactionLogsRepository.existsById(id)) {
            throw new EntityNotFoundException("No se encontró el log con ID: " + id);
        }
        transactionLogsRepository.deleteById(id);
    }

    /**
     * Cuenta la cantidad de logs por acción
     * @param action tipo de acción
     * @return cantidad de logs para la acción especificada
     */
    public long countLogsByAction(String action) {
        return transactionLogsRepository.countByAction(action);
    }

    /**
     * Cuenta la cantidad de logs para una transacción
     * @param transactionId ID de la transacción
     * @return cantidad de logs para la transacción especificada
     */
    public long countLogsByTransaction(UUID transactionId) {
        return transactionLogsRepository.countByTransactionId(transactionId);
    }

    /**
     * Obtiene los logs más recientes
     * @param limit cantidad máxima de logs a retornar
     * @return lista de logs ordenados por fecha (más recientes primero)
     */
    public List<TransactionLogs> getRecentLogs(int limit) {
        // Implementación directa si es un número limitado (ej. 10)
        if (limit <= 10) {
            return transactionLogsRepository.findTop10ByOrderByTimestampDesc();
        }

        // Si necesitamos más de 10, hacemos una implementación alternativa
        List<TransactionLogs> allLogs = transactionLogsRepository.findAll();
        allLogs.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));

        if (limit > 0 && limit < allLogs.size()) {
            return allLogs.subList(0, limit);
        }

        return allLogs;
    }

    /**
     * Elimina logs antiguos (mantenimiento)
     * @param before fecha límite: se eliminarán los logs anteriores a esta fecha
     * @return cantidad de logs eliminados
     */
    @Transactional
    public int deleteOldLogs(Timestamp before) {
        // Contar logs a eliminar (para retornar la cantidad)
        List<TransactionLogs> logsToDelete = transactionLogsRepository.findByTimestampBefore(before);
        int count = logsToDelete.size();

        // Eliminar logs antiguos
        transactionLogsRepository.deleteByTimestampBefore(before);

        return count;
    }

    /**
     * Busca logs en un rango de fechas
     * @param startDate fecha inicial
     * @param endDate fecha final
     * @return lista de logs en el rango especificado
     */
    public List<TransactionLogs> getLogsBetweenDates(Timestamp startDate, Timestamp endDate) {
        return transactionLogsRepository.findByTimestampBetween(startDate, endDate);
    }

    /**
     * Busca logs por múltiples tipos de acción
     * @param actions lista de tipos de acción
     * @return lista de logs que corresponden a cualquiera de las acciones especificadas
     */
    public List<TransactionLogs> getLogsByMultipleActions(List<String> actions) {
        return transactionLogsRepository.findByActionIn(actions);
    }
}
