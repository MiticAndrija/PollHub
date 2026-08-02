package com.pollhub.service;

import com.pollhub.dto.*;
import com.pollhub.entity.*;
import com.pollhub.exception.ResourceNotFoundException;
import com.pollhub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service @RequiredArgsConstructor
public class PollService {
    private final PollRepository pollRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    @Transactional
    public PollResponse create(PollRequest request, String email) {
        Poll poll = new Poll();
        apply(poll, request);
        poll.setCreator(user(email)); poll.setCreatedAt(LocalDateTime.now()); poll.setActive(true);
        return response(pollRepository.save(poll));
    }

    @Transactional(readOnly=true)
    public List<PollResponse> search(String search, Long categoryId, Boolean active, String sort) {
        return pollRepository.search(blankToNull(search), categoryId, active, "popular".equals(sort) ? "popular" : "newest")
                .stream().map(this::response).toList();
    }

    @Transactional(readOnly=true) public PollResponse get(Long id) { return response(poll(id)); }

    @Transactional(readOnly=true)
    public List<PollResponse> mine(String email) {
        return pollRepository.findByCreatorId(user(email).getId()).stream().map(this::response).toList();
    }

    @Transactional
    public PollResponse update(Long id, PollRequest request, String email) {
        Poll poll = owned(id,email); apply(poll,request); return response(poll);
    }

    @Transactional public PollResponse close(Long id,String email) { Poll poll=owned(id,email); poll.setActive(false); return response(poll); }
    @Transactional public void delete(Long id,String email) { pollRepository.delete(owned(id,email)); }
    @Transactional public void adminDelete(Long id) { pollRepository.delete(poll(id)); }

    private void apply(Poll poll, PollRequest request) {
        poll.setTitle(request.title().trim()); poll.setDescription(trim(request.description()));
        poll.setCategory(categoryRepository.findById(request.categoryId()).orElseThrow(() -> new ResourceNotFoundException("Category not found")));
        poll.setExpiresAt(request.expiresAt());
        poll.getOptions().clear();
        request.options().stream().map(String::trim).distinct().forEach(text -> { PollOption o=new PollOption(); o.setText(text); o.setPoll(poll); poll.getOptions().add(o); });
        if (poll.getOptions().size()<2) throw new IllegalArgumentException("At least two different options are required");
    }
    private Poll owned(Long id,String email) { Poll p=poll(id); if(!p.getCreator().getEmail().equalsIgnoreCase(email)) throw new AccessDeniedException("Poll belongs to another user"); return p; }
    private Poll poll(Long id){ return pollRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Poll not found")); }
    private User user(String email){ return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found")); }
    private PollResponse response(Poll p){
        boolean active=p.isActive() && (p.getExpiresAt()==null || p.getExpiresAt().isAfter(LocalDateTime.now()));
        long votes=voteRepository.countByPollId(p.getId());
        return new PollResponse(p.getId(),p.getTitle(),p.getDescription(),active,p.getCreatedAt(),p.getExpiresAt(),p.getCreator().getId(),
                p.getCreator().getFirstName()+" "+p.getCreator().getLastName(),p.getCategory().getId(),p.getCategory().getName(),
                p.getOptions().stream().map(o->new PollOptionResponse(o.getId(),o.getText())).toList(),votes);
    }
    private String trim(String value){return value==null?null:value.trim();}
    private String blankToNull(String value){return value==null||value.isBlank()?null:value.trim();}
}
