package com.ubuntuhealth.scheduler;

import com.ubuntuhealth.model.Reminder;
import com.ubuntuhealth.service.ReminderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReminderScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ReminderScheduler.class);
    private final ReminderService reminderService;

    @Autowired
    public ReminderScheduler(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    /**
     * This method runs automatically every 60 seconds (60000 milliseconds).
     * It checks for pending appointment or medication reminders and "sends" them.
     */
    @Scheduled(fixedRate = 60000)
    public void sendScheduledReminders() {
        logger.info("Checking database for scheduled reminders...");

        List<Reminder> pendingReminders = reminderService.getPendingRemindersToSend();

        if (pendingReminders.isEmpty()) {
            logger.info("No reminders due at this time.");
            return;
        }

        for (Reminder reminder : pendingReminders) {
            // Simulate sending a reminder (In a production system, this is where you'd call an SMS or Email API)
            logger.info("SENDING REMINDER: To: {} {} | Message: '{}' | Type: {}",
                    reminder.getPatient().getName(),
                    reminder.getPatient().getSurname(),
                    reminder.getMessage(),
                    reminder.getType());

            // Mark the reminder as sent in the database so it isn't sent again
            reminderService.markAsSent(reminder.getId());
        }
    }
}