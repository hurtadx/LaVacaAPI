package com.lavacaapi.lavaca.profiles.controller;

import com.lavacaapi.lavaca.profiles.Profiles;
import com.lavacaapi.lavaca.profiles.service.ProfilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")
public class ProfilesController {

    @Autowired
    private ProfilesService profilesService;

    @PostMapping
    public ResponseEntity<Profiles> createProfile(@RequestBody Profiles profile) {
        return new ResponseEntity<>(profilesService.createProfile(profile), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Profiles>> getAllProfiles() {
        return ResponseEntity.ok(profilesService.getAllProfiles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profiles> getProfileById(@PathVariable UUID id) {
        return profilesService.getProfileById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Profiles> getProfileByEmail(@PathVariable String email) {
        return profilesService.getProfileByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Profiles> getProfileByUsername(@PathVariable String username) {
        return profilesService.getProfileByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<Map<String, Object>> getPublicProfile(@PathVariable UUID id) {
        return ResponseEntity.ok(profilesService.getPublicProfile(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profiles> updateProfile(@PathVariable UUID id, @RequestBody Profiles profile) {
        return ResponseEntity.ok(profilesService.updateProfile(id, profile));
    }

    @PutMapping("/{id}/email")
    public ResponseEntity<Profiles> changeEmail(@PathVariable UUID id, @RequestBody Map<String, String> emailData) {
        return ResponseEntity.ok(profilesService.changeEmail(id, emailData.get("email")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable UUID id) {
        profilesService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }
}
