package com.pollhub.controller;
import com.pollhub.dto.*; import com.pollhub.service.*; import lombok.RequiredArgsConstructor; import org.springframework.http.HttpStatus; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/admin") @RequiredArgsConstructor public class AdminController {
 private final AdminService admin; private final PollService polls;
 @GetMapping("/users") public List<AdminUserResponse> users(){return admin.users();}
 @PatchMapping("/users/{id}/enabled") public AdminUserResponse enabled(@PathVariable Long id,@RequestParam boolean enabled){return admin.setEnabled(id,enabled);}
 @GetMapping("/polls") public List<PollResponse> polls(){return polls.search(null,null,null,"newest");}
 @DeleteMapping("/polls/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){polls.adminDelete(id);}
 @GetMapping("/statistics") public AdminStatisticsResponse statistics(){return admin.statistics();}
}
