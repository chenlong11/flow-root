package com.nuvole.flow.mapper;

import com.fasterxml.jackson.databind.node.ArrayNode;

public interface InfoMapper {

    ArrayNode map(Object element);
}
