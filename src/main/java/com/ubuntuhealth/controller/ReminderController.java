package com.ubuntuhealth.controller;

import com.ubuntuhealth.model.Reminder;
import com.ubuntuhealth.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@CrossOrigin(origins = "*")
public class ReminderController {

    private final ReminderService reminderService;

    @Autowired
    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    // Endpoint for Admin to schedule a new reminder
    @PostMapping
    public ResponseEntity<Reminder> scheduleReminder(@RequestBody Reminder reminder) {
        Reminder created = reminderService.scheduleReminder(reminder);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Endpoint for patient's dashboard to fetch their own reminders
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<Reminder>> getMyReminders(@PathVariable Long patientId) {
        List<Reminder> reminders = reminderService.getRemindersForPatient(patientId);
        return ResponseEntity.ok(reminders);
    }
}