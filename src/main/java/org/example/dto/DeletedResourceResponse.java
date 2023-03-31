package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@AllArgsConstructor
@Builder
public class DeletedResourceResponse {
    List<Integer> ids;
}
