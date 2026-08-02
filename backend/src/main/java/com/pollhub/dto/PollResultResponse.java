package com.pollhub.dto;
import java.util.List;
public record PollResultResponse(Long pollId,String pollTitle,long totalVotes,List<OptionResult> options) {
 public record OptionResult(Long optionId,String text,long votes,double percentage) {}
}
