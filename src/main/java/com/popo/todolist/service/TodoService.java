package com.popo.todolist.service;

import com.popo.todolist.common.RootException;
import com.popo.todolist.common.constants.ResultCodeType;
import com.popo.todolist.entity.TodoEntity;
import com.popo.todolist.entity.UserEntity;
import com.popo.todolist.model.request.TodoCreateRequestDto;
import com.popo.todolist.repository.TodoRepository;
import com.popo.todolist.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoService {
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public void createTodo(Long id, TodoCreateRequestDto todoCreateRequestDto) {
        validateRequest(todoCreateRequestDto.getTitle());
        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new RootException(ResultCodeType.SERVER_ERROR_4S000000));

        TodoEntity todoEntity = new TodoEntity(userEntity, todoCreateRequestDto);
        todoRepository.save(todoEntity);
    }

    private void validateRequest(String title) {
        if (StringUtils.isEmpty(title)) {
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }
    }
}
