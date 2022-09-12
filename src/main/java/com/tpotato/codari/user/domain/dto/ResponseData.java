package com.tpotato.codari.user.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Data
public class ResponseData {
  private final Object resultData;
  private final String statusMsg;
  private final String statusCause;
}
