package com.example.userTasks.service;

import com.example.userTasks.model.Task;
import com.example.userTasks.model.User;
import com.example.userTasks.repository.TaskRepository;
import com.example.userTasks.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Service
public class PutTaskForUsers {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    private TreeMap<Integer, ArrayList<Integer>> countTaskPerUser = new TreeMap<>();
    private TreeMap<Integer, ArrayList<Integer>> result = new TreeMap<>();
    private ArrayList<Integer> currentListAllUsers;

    private ArrayList<Integer> nextListModUsers;

    private int currentCount;
    private int currentResultKey;
    private int firstElement;

    public TreeMap<Integer, ArrayList<Integer>> putTasks(int offlineUserId) {
        int countUnsignedTask =  prepareData(offlineUserId);
        fillLists(countUnsignedTask);

        return countTaskPerUser;
    }

    private void fillLists(int countTasks) {

        for (int i = 1; i <= countTasks; i++) {
            if (countTaskPerUser.firstEntry().getValue().size() == 0) {
                countTaskPerUser.remove(currentCount);
            }
            initStep();

            // сохраняем текущую пару без 0 значения в списке
            countTaskPerUser.replace(currentCount, currentListAllUsers);

            // добавляем первый элемент (который уже удалил из списка)
            if (nextListModUsers == null) {
                result.put(currentResultKey, new ArrayList<>(List.of(firstElement)));
            } else {
                nextListModUsers.add(firstElement);
                result.replace(currentResultKey, nextListModUsers);
            }
        }
        log.info("after processing unsigned tasks. receive map (changes in users): " + result);
    }

    private int prepareData(int offlineUserId) {

        List<Task> unsignedTasks = taskRepository.findByUserId(offlineUserId);
        List<User> usersOnline = userRepository.findByStatus(User.Status.Online);

        if (usersOnline.size() > 0 && unsignedTasks.size() > 0) {
            log.info("start assign tasks to another online users.");
            log.info("count unsigned tasks: {}, count another online users: {}",
                    unsignedTasks.size(), usersOnline.size());

            for (User user : usersOnline) {

                int userId = user.getId();
                int currentKey = taskRepository.findByUserId(userId).size();

                // если пара существует (запоминаем)
                if (countTaskPerUser.get(currentKey) != null) {
                    ArrayList<Integer> currentListUsers = countTaskPerUser.get(currentKey);
                    currentListUsers.add(userId);
                    countTaskPerUser.replace(currentKey, currentListUsers);
                } else {
                    countTaskPerUser.put(currentKey, new ArrayList<>(List.of(userId)));
                }
            }
            log.info("after prepare data, receive map tasks per user: {}", countTaskPerUser);
        }

        return unsignedTasks.size();
    }

    private void initStep() {
        currentCount = countTaskPerUser.firstKey();
        currentListAllUsers = countTaskPerUser.get(currentCount);

        firstElement = currentListAllUsers.get(0);

        if (result.firstEntry() != null) {
            currentResultKey = countTaskPerUser.firstKey() + 1;
        } else {
            currentResultKey = 1;
        }
        nextListModUsers = result.get(currentResultKey);

        currentListAllUsers.remove(0);
    }
}
