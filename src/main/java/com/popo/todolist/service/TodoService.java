package com.popo.todolist.service;

import com.popo.todolist.common.RootException;
import com.popo.todolist.common.constants.ResultCodeType;
import com.popo.todolist.entity.TodoEntity;
import com.popo.todolist.entity.UserEntity;
import com.popo.todolist.entity.constatns.TodoStatus;
import com.popo.todolist.model.request.TodoUpdateContentRequestDto;
import com.popo.todolist.model.request.UpdateTodoStatusRequestDto;
import com.popo.todolist.model.request.TodoCreateRequestDto;
import com.popo.todolist.model.response.TodoListResponse;
import com.popo.todolist.model.response.TodoResponse;
import com.popo.todolist.repository.TodoRepository;
import com.popo.todolist.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.popo.todolist.entity.constatns.TodoStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoService {
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public void createTodo(Long userId, TodoCreateRequestDto todoCreateRequestDto) {
        validateRequest(todoCreateRequestDto.getTitle());
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RootException(ResultCodeType.SERVER_ERROR_4S000000));

        TodoEntity todoEntity = new TodoEntity(userEntity, todoCreateRequestDto);
        todoRepository.save(todoEntity);
    }

    @Transactional
    public void updateTodoStatus(Long userId, UpdateTodoStatusRequestDto updateTodoStatusRequestDto) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RootException(ResultCodeType.SERVER_ERROR_4S000000));
        TodoEntity todoEntity = validateAndGetTodoEntity(updateTodoStatusRequestDto.getId(), userEntity);

        TodoStatus currentStatus = todoEntity.getTodoStatus();
        validateStateTransition(currentStatus, updateTodoStatusRequestDto.getStatus());

        todoEntity.updateStatus(updateTodoStatusRequestDto.getStatus());
    }

    @Transactional
    public void updateTodoContent(Long userId, TodoUpdateContentRequestDto updateTodoContentRequestDto) {
        validateRequest(updateTodoContentRequestDto.getTitle());

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RootException(ResultCodeType.SERVER_ERROR_4S000000));
        TodoEntity todoEntity = validateAndGetTodoEntity(updateTodoContentRequestDto.getId(), userEntity);

        todoEntity.updateContent(updateTodoContentRequestDto.getTitle(), updateTodoContentRequestDto.getDescription());
    }


    @Transactional(readOnly = true)
    public TodoListResponse getAllTodos(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RootException(ResultCodeType.SERVER_ERROR_4S000000));
        List<TodoEntity> allTodos = userEntity.getTodoEntityList();

        TodoEntity latestTodo = allTodos.stream()
                .max(Comparator.comparing(TodoEntity::getCreateDt))
                .orElse(null);

        TodoResponse latest = new TodoResponse(latestTodo);
        List<TodoResponse> todoList = filterByStatus(allTodos, TODO);
        List<TodoResponse> inProgressList = filterByStatus(allTodos, IN_PROGRESS);
        List<TodoResponse> pendingList = filterByStatus(allTodos, PENDING);
        List<TodoResponse> doneList = filterByStatus(allTodos, DONE);
        return new TodoListResponse(latest, todoList, inProgressList, pendingList, doneList);
    }

    private List<TodoResponse> filterByStatus(List<TodoEntity> todos, TodoStatus status) {
        return todos.stream()
                .filter(todo -> todo.getTodoStatus() == status)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private TodoResponse convertToDto(TodoEntity todoEntity) {
        return new TodoResponse(todoEntity.getId(), todoEntity.getTitle(), todoEntity.getDescription(), todoEntity.getTodoStatus().toString());
    }

    private TodoEntity validateAndGetTodoEntity(Long requestId, UserEntity userEntity) {
        Optional<TodoEntity> todoEntityOptional = userEntity.getTodoEntityList().stream()
                .filter(todo -> todo.getId().equals(requestId))
                .findFirst();
        if (todoEntityOptional.isEmpty()) {
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }

        return todoEntityOptional.get();
    }

    private void validateRequest(String title) {
        if (StringUtils.isEmpty(title)) {
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }
    }

    private void validateStateTransition(TodoStatus currentStatus, TodoStatus nextStatus) {
        switch (currentStatus) {
            case TODO:
                validateTransitionFromTodo(nextStatus);
                break;
            case IN_PROGRESS:
                validateTransitionFromInProgress(nextStatus);
                break;
            case PENDING:
                // PENDING 상태 일경우 어떤 상태로든 변경 가능.
                break;
            case DONE:
                // DONE 상태 일경우 변경 불가.
                throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }
    }

    private void validateTransitionFromTodo(TodoStatus nextStatus) {
        //  상태에서 가능한 다음 상태는 IN_PROGRESS, DONE, PENDING
        if (nextStatus == TODO) {
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }
    }

    private void validateTransitionFromInProgress(TodoStatus nextStatus) {
        if (nextStatus == IN_PROGRESS) {
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }

        // IN_PROGRESS 상태에서 변경 가능한 다음 상태는 PENDING, DONE
        if (!(nextStatus == PENDING || nextStatus == DONE)) {
            throw new RootException(ResultCodeType.SERVER_ERROR_4S000000);
        }
    }


}
