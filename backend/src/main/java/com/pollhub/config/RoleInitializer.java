package com.pollhub.config;

import com.pollhub.entity.Role;
import com.pollhub.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class RoleInitializer {

    private final RoleRepository roleRepository;

    @Bean
    ApplicationRunner initializeRoles() {
        return args -> createRoleIfMissing("ROLE_USER");
    }

    @Transactional
    public void createRoleIfMissing(String roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
        if ("ROLE_USER".equals(roleName)) {
            createRoleIfMissing("ROLE_ADMIN");
        }
    }
}
