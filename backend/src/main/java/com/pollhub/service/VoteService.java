package com.pollhub.service;
import com.pollhub.dto.*;
import com.pollhub.entity.*;
import com.pollhub.exception.*;
import com.pollhub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class VoteService {
 private final VoteRepository voteRepository; private final PollRepository pollRepository;
 private final PollOptionRepository optionRepository; private final UserRepository userRepository;
 @Transactional public VoteResponse vote(Long pollId, VoteRequest request, String email){
  Poll p=pollRepository.findById(pollId).orElseThrow(()->new ResourceNotFoundException("Poll not found"));
  if(!p.isActive() || (p.getExpiresAt()!=null && !p.getExpiresAt().isAfter(LocalDateTime.now()))) throw new BusinessRuleException("Poll is not active");
  User u=userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User not found"));
  if(voteRepository.existsByUserIdAndPollId(u.getId(),pollId)) throw new BusinessRuleException("User has already voted in this poll");
  PollOption o=optionRepository.findByIdAndPollId(request.optionId(),pollId).orElseThrow(()->new BusinessRuleException("Option does not belong to this poll"));
  Vote v=new Vote(); v.setPoll(p);v.setPollOption(o);v.setUser(u);v.setCreatedAt(LocalDateTime.now());
  try { v=voteRepository.saveAndFlush(v); } catch(DataIntegrityViolationException e){throw new BusinessRuleException("User has already voted in this poll");}
  return new VoteResponse(v.getId(),pollId,o.getId(),v.getCreatedAt());
 }
 @Transactional(readOnly=true) public PollResultResponse results(Long pollId){
  Poll p=pollRepository.findById(pollId).orElseThrow(()->new ResourceNotFoundException("Poll not found")); long total=voteRepository.countByPollId(pollId);
  var options=p.getOptions().stream().map(o->{long count=voteRepository.countByPollOptionId(o.getId()); double pct=total==0?0:Math.round(count*10000.0/total)/100.0; return new PollResultResponse.OptionResult(o.getId(),o.getText(),count,pct);}).toList();
  return new PollResultResponse(p.getId(),p.getTitle(),total,options);
 }
}
