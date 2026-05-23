package com.melle.flightbooking.seeders;

import com.melle.flightbooking.model.RoleEnum;
import com.melle.flightbooking.model.User;
import com.melle.flightbooking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (userRepository.count() > 0) {
            return; // voorkomt dubbele seed
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("Admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("admin1234"));
        admin.setRole(RoleEnum.ADMIN);

        User user = new User();
        user.setUsername("user");
        user.setEmail("User@gmail.com");
        user.setPassword(passwordEncoder.encode("user1234"));
        user.setRole(RoleEnum.USER);

        userRepository.save(admin);
        userRepository.save(user);

        System.out.println("Dummy users seeded!");
    }
}
