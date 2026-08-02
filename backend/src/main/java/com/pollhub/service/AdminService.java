package com.pollhub.service;
import com.pollhub.dto.*; import com.pollhub.entity.*; import com.pollhub.exception.*; import com.pollhub.repository.*;
import lombok.RequiredArgsConstructor; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.*;
@Service @RequiredArgsConstructor public class AdminService {
 private final UserRepository users; private final PollRepository polls; private final VoteRepository votes;
 @Transactional(readOnly=true) public List<AdminUserResponse> users(){return users.findAll().stream().map(u->new AdminUserResponse(u.getId(),u.getFirstName(),u.getLastName(),u.getEmail(),u.isEnabled(),u.getRoles().stream().map(Role::getName).collect(java.util.stream.Collectors.toSet()),u.getCreatedAt())).toList();}
 @Transactional public AdminUserResponse setEnabled(Long id,boolean enabled){User u=users.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));u.setEnabled(enabled);return new AdminUserResponse(u.getId(),u.getFirstName(),u.getLastName(),u.getEmail(),u.isEnabled(),u.getRoles().stream().map(Role::getName).collect(java.util.stream.Collectors.toSet()),u.getCreatedAt());}
 @Transactional(readOnly=true) public AdminStatisticsResponse statistics(){return new AdminStatisticsResponse(users.count(),polls.count(),polls.countCurrentlyActive(),votes.count());}
}
