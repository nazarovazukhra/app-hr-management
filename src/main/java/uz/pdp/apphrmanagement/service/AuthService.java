package uz.pdp.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.pdp.apphrmanagement.entity.Position;
import uz.pdp.apphrmanagement.entity.Role;
import uz.pdp.apphrmanagement.entity.User;
import uz.pdp.apphrmanagement.payload.ApiResponse;
import uz.pdp.apphrmanagement.payload.LoginDto;
import uz.pdp.apphrmanagement.payload.RegisterDto;
import uz.pdp.apphrmanagement.repository.PositionRepository;
import uz.pdp.apphrmanagement.repository.RoleRepository;
import uz.pdp.apphrmanagement.repository.UserRepository;
import uz.pdp.apphrmanagement.security.JwtProvider;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    PositionRepository positionRepository;


    public ApiResponse registerUser(RegisterDto registerDto) {

        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail)
            return new ApiResponse("Bunday email allaqachon mavjud", false);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal().equals("anonymousUser")) {
            Set<Role> roleForAnonymousUser = registerDto.getRoleName();
            for (Role role : roleForAnonymousUser) {
                if (role.getRoleName().equalsIgnoreCase("director")) {
                    User user = saveUserData(registerDto);
                    user.setEnabled(true);
                    user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
                    userRepository.save(user);
                    return new ApiResponse("Director added", true);
                }
            }
        } else {
            User principal = (User) authentication.getPrincipal();

            Set<Role> roles = principal.getRoles();
            Set<Position> positions = principal.getPositions();

            for (Role role : roles) {
                for (Position position : positions) {

                    Set<Role> addingUser = registerDto.getRoleName();
                    if (role.getRoleName().equalsIgnoreCase("worker"))
                        return new ApiResponse("Worker can not add any user", false);
                    if (role.getRoleName().equalsIgnoreCase("director") && position.getPositionNumber() == 1) {
                        for (Role userRole : addingUser) {
                            String roleNameForUser = userRole.getRoleName();
                            if (roleNameForUser.equalsIgnoreCase("worker")) {

                                User user2 = saveUserData(registerDto);
                                user2.setPassword(passwordEncoder.encode(registerDto.getPassword()));
                                userRepository.save(user2);
                                sendEmail(user2.getEmail(), user2.getEmailCode());

                                return new ApiResponse("Worker added", true);
                            } else if (roleNameForUser.equalsIgnoreCase("manager")) {

                                User user2 = saveUserData(registerDto);
                                user2.setPassword(passwordEncoder.encode(registerDto.getPassword()));
                                userRepository.save(user2);
                                sendEmail(user2.getEmail(), user2.getEmailCode());

                                return new ApiResponse("Manager added", true);
                            } else {
                                return new ApiResponse("Only director can add" + roleNameForUser, false);
                            }
                        }
                    }
                    if (role.getRoleName().equalsIgnoreCase("manager") && position.getPositionNumber() == 2) {
                        for (Role userRole : addingUser) {
                            if (userRole.getRoleName().equalsIgnoreCase("worker")) {

                                User user2 = saveUserData(registerDto);
                                user2.setPassword(passwordEncoder.encode(registerDto.getPassword()));
                                userRepository.save(user2);
                                sendEmail(user2.getEmail(), user2.getEmailCode());

                                return new ApiResponse("Worker added", true);
                            } else {

                                return new ApiResponse(role.getRoleName() + " can not add " + userRole.getRoleName(), false);
                            }
                        }
                    }
                }
            }
        }
        return new ApiResponse("Error in server", false);
    }

    public User saveUserData(RegisterDto registerDto) {


        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        //   user.setPassword(passwordEncoder.encode(""));
        user.setRoles(registerDto.getRoleName());
        user.setPositions(registerDto.getPosition());

        //user.setRoles(roleRepository.findAllByRoleName(String.valueOf(registerDto.getRoleName())));

        user.setEmailCode(UUID.randomUUID().toString());

        //userRepository.save(user);

        // EMAILGA YUBORISH METHOD I
        return user;
    }

    public Boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("nazarovazuhra356@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Account ni Tasdiqlash");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail + "'>Tasdiqlang</a>");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findAllByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Account tasdiqlandi", true);
        }
        return new ApiResponse("Account allaqachon tasdiqlangan", false);

    }

    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (loginDto.getUsername(), loginDto.getPassword()));
            User user = (User) authentication.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getUsername(), user.getRoles());
            return new ApiResponse("Token", true, token);
        } catch (BadCredentialsException badCredentialsException) {
            return new ApiResponse("Parol yoki login xato", false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findAllByEmail(username);
//        if (optionalUser.isPresent()) {
//            return optionalUser.get();
//        }
//        throw new UsernameNotFoundException(username + "topilmadi");

        return userRepository.findAllByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + "topilmadi"));
    }

}
