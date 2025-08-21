package com.ecommerce.user.service;

import com.ecommerce.user.dto.AddressDTO;
import com.ecommerce.user.dto.UserRequest;
import com.ecommerce.user.dto.UserResponse;
import com.ecommerce.user.model.Address;
import com.ecommerce.user.model.User;
import com.ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserResponseDTO)
                .collect(Collectors.toList());
    }

    public void addUser(UserRequest user) {
        User newUser = new User();
        updateUserFromUserRequest(user, newUser);
        userRepository.save(newUser);
    }

    public Optional<UserResponse> fetchUser(Long id) {
        return userRepository.findById(id)
                .map(this::mapUserResponseDTO);
    }

    public boolean updateUser(Long id, UserRequest updatedUser) {
        Optional<User> existingUserOptional = userRepository.findById(id);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();
            updateUserFromUserRequest(updatedUser, existingUser);
            userRepository.save(existingUser);
            return true;
        } else {
            return false;
        }
    }

    private void updateUserFromUserRequest(UserRequest user, User newUser) {
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPhone(user.getPhone());
        if (user.getAddress() != null) {
            AddressDTO addressDTO = user.getAddress();
            Address address = new Address();
            address.setStreet(addressDTO.getStreet());
            address.setCity(addressDTO.getCity());
            address.setState(addressDTO.getState());
            address.setCountry(addressDTO.getCountry());
            address.setZipcode(addressDTO.getZipcode());
            newUser.setAddress(address);
        }
    }

    private UserResponse mapUserResponseDTO(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());

        if (user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            Address address = user.getAddress();
            addressDTO.setStreet(address.getStreet());
            addressDTO.setCity(address.getCity());
            addressDTO.setState(address.getState());
            addressDTO.setCountry(address.getCountry());
            addressDTO.setZipcode(address.getZipcode());
            userResponse.setAddress(addressDTO);
        }
        return userResponse;
    }
}
