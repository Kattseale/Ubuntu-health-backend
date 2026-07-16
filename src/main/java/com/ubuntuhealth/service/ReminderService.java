package com.ubuntuhealth.service;

import com.ubuntuhealth.model.Reminder;
import com.ubuntuhealth.model.User;
import com.ubuntuhealth.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;

    @Autowired
    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    // Schedule a new reminder (e.g. when booking an appointment)
    public Reminder scheduleReminder(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    // Get active reminders specifically for one patient's dashboard
    public List<Reminder> getRemindersForPatient(Long patientId) {
        return reminderRepository.findByPatientId(patientId);
    }

    // Fetch reminders that are due to be sent out right now
    public List<Reminder> getPendingRemindersToSend() {
        return reminderRepository.findByIsSentFalseAndScheduledTimeBefore(LocalDateTime.now());
    }

    // Mark a reminder as sent so we don't send it twice
    public void markAsSent(Long reminderId) {
        reminderRepository.findById(reminderId).ifPresent(reminder -> {
            reminder.setSent(true);
            reminderRepository.save(reminder);
        });
    }
}