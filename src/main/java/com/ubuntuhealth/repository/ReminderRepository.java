package com.ubuntuhealth.repository;

import com.ubuntuhealth.model.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    // Used to find reminders that are scheduled to be sent but haven't been processed yet
    List<Reminder> findByIsSentFalseAndScheduledTimeBefore(LocalDateTime time);

    // Used to display a patient's personal reminders on their dashboard
    List<Reminder> findByPatientId(Long patientId);
}