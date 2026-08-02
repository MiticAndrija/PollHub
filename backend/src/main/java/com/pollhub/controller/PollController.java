package com.pollhub.controller;
import com.pollhub.dto.*; import com.pollhub.service.*; import jakarta.validation.Valid; import lombok.RequiredArgsConstructor; import org.springframework.http.*; import org.springframework.web.bind.annotation.*; import java.util.List; import java.security.Principal;
@RestController @RequestMapping("/api/polls") @RequiredArgsConstructor public class PollController {
 private final PollService service; private final VoteService votes;
 @GetMapping public List<PollResponse> all(@RequestParam(required=false)String search,@RequestParam(required=false)Long categoryId,@RequestParam(required=false)Boolean active,@RequestParam(defaultValue="newest")String sort){return service.search(search,categoryId,active,sort);}
 @GetMapping("/{id}") public PollResponse get(@PathVariable Long id){return service.get(id);}
 @GetMapping("/mine") public List<PollResponse> mine(Principal a){return service.mine(a.getName());}
 @PostMapping public ResponseEntity<PollResponse> create(@Valid @RequestBody PollRequest r,Principal a){return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r,a.getName()));}
 @PutMapping("/{id}") public PollResponse update(@PathVariable Long id,@Valid @RequestBody PollRequest r,Principal a){return service.update(id,r,a.getName());}
 @PatchMapping("/{id}/close") public PollResponse close(@PathVariable Long id,Principal a){return service.close(id,a.getName());}
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id,Principal a){service.delete(id,a.getName());}
 @PostMapping("/{id}/votes") public ResponseEntity<VoteResponse> vote(@PathVariable Long id,@Valid @RequestBody VoteRequest r,Principal a){return ResponseEntity.status(HttpStatus.CREATED).body(votes.vote(id,r,a.getName()));}
 @GetMapping("/{id}/results") public PollResultResponse results(@PathVariable Long id){return votes.results(id);}
}
