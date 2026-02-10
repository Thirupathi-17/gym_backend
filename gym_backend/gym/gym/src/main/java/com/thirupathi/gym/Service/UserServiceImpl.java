package com.thirupathi.gym.Service;

import com.thirupathi.gym.Config.JwtProvider;
import com.thirupathi.gym.Dto.UserDto;
import com.thirupathi.gym.Entity.User;
import com.thirupathi.gym.Repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;



    public ResponseEntity<?> createUser(User user){

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException(" User with email " + user.getEmail() + " already exists!");
        }

        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new RuntimeException(" User with Phone " + user.getPhoneNumber() + " already exists!");
        }

        try{
            User newUser = new User();
            newUser.setUserName( user.getUserName() );
            newUser.setEmail( user.getEmail() );
            newUser.setPassword( passwordEncoder.encode(user.getPassword()));
            newUser.setPhoneNumber(user.getPhoneNumber());
            newUser.setCreatedAt(LocalDateTime.now().withNano(0));
            newUser.setUpdatedAt(LocalDateTime.now().withNano(0));
            if (user.getProfilePic() == null){
                newUser.setProfilePic("https://www.svgrepo.com/show/384670/account-avatar-profile-user.svg");
            }else{
                newUser.setProfilePic( user.getProfilePic());
            }
            if (user.getUserRole() == null){
                newUser.setUserRole(user.getUserRole());
            }else{
                newUser.setUserRole( user.getUserRole());
            }
            userRepository.save( newUser );
            return ResponseEntity.ok().body(" User Created Successfully ... " );
        }catch(Exception err){
            return ResponseEntity.badRequest().body(" Error Message : " + err.getMessage());
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public User updateUser(Long id, User updatedUser) {
        User user = getUserById(id);
        user.setUserName(updatedUser.getUserName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    public ResponseEntity<?> login(User user, HttpServletResponse response) {

        User existingUser = userRepository.findByEmail(user.getEmail()).
                orElseThrow( () -> new RuntimeException(" User not Found..."));

        System.out.println(existingUser);
        if( !passwordEncoder.matches( user.getPassword(), existingUser.getPassword() )){
            return ResponseEntity.badRequest().body(" Wrong Credentials");
        }

        String token = jwtProvider.generatedToken(existingUser.getUserName(), existingUser.getUserRole());
        UserDto loginResponse = new UserDto();
        loginResponse.setUserId(existingUser.getUserId());
        loginResponse.setUsername(existingUser.getUserName());
        loginResponse.setEmail(existingUser.getEmail());
        loginResponse.setAddress(existingUser.getAddress());
        loginResponse.setPincode(existingUser.getPincode());
        loginResponse.setProfilePic(existingUser.getProfilePic());
        loginResponse.setPhoneNumber(existingUser.getPhoneNumber());
        loginResponse.setUserRole( existingUser.getUserRole());
        loginResponse.setCreatedAt( existingUser.getCreatedAt());
        loginResponse.setToken( token );

        // To Check whether the Login page take the Free trail or not
       // activateFreeTrial(loginResponse);


        Cookie cookie = new Cookie("access_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);

        UserDto locationDetails = getPincode(loginResponse.getPincode(), loginResponse.getUserId());
        loginResponse.setCity(locationDetails.getCity());
        loginResponse.setState(locationDetails.getState());
        loginResponse.setCountry(locationDetails.getCountry());
        return ResponseEntity.ok().body(loginResponse);
    }

    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("access_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
        return ResponseEntity.ok("User logout Successfull " );
    }

    public UserDto getPincode(Long pincode, Long loginUserId) {

        System.out.println(pincode);

        String url = "https://nominatim.openstreetmap.org/search?format=json&q=" + pincode + " India";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<Map<String, Object>>> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                );

        if (response.getBody() == null || response.getBody().isEmpty()) {
            throw new RuntimeException("Invalid pincode: " + pincode);
        }

        Map<String, Object> data = response.getBody().get(0);

        String address = data.get("display_name").toString();

        System.out.println(address);
        String[] parts = address.split(",");

        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        int len = parts.length;
        String country = len >= 1 ? parts[len - 1] : null;
        String state   = len >= 2 ? parts[len - 2] : null;
        String city    = len >= 3 ? parts[len - 3] : null;

        System.out.println(country);
        System.out.println(state);
        System.out.println(city);

        User loginUser = userRepository.findById(loginUserId).orElseThrow(
                () -> new RuntimeException(" Login user not Found ...!")
        );

        UserDto location = new UserDto();
        location.setCity(city);
        location.setState(state);
        location.setCountry(country);
        return location;
    }

}
