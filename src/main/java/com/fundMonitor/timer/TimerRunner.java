package com.fundMonitor.timer;

import com.fundMonitor.entity.Log;
import com.fundMonitor.entity.Task;
import com.fundMonitor.repository.TaskRepository;
import com.fundMonitor.service.LogService;
import com.fundMonitor.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

@Component
public class TimerRunner {

    @Autowired
    TaskService taskService;

    @Autowired
    LogService logService;

    // 1min检测任务是否完成
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void finTask() {

    }

    // 1min把日志文件更新到数据库
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void updateLog(){
        List<Task> tasks = taskService.getWaitingTasks();
        for (Task task : tasks) {
            String logPath = task.getLogPath();
            Long lastTimeFileSize = task.getLogLength();
            if (logPath != null) {
                File logFile = new File(logPath);
                long len = logFile.length();
                RandomAccessFile raf = null;
                if (len <= lastTimeFileSize) {
                    System.out.println(logFile + "日志并没有任何新的输入");
                } else if (len > lastTimeFileSize) {
                    try {
                        raf = new RandomAccessFile(logFile, "r");
                        raf.seek(lastTimeFileSize+1);
                        String line = null;
                        while ((line = raf.readLine()) != null) {
                            if("".equals(line)){
                                continue;
                            }
                            //System.out.println(logFile + "的数据:" + line);
                            String[] split = line.split("] ");
                            Log log = new Log();
                            log.setTime(split[0].replace("]", "").replace("[", ""));
                            log.setLevel(split[1].replace("]", "").replace("[", ""));
                            log.setLoggerName(split[2].replace("]", "").replace("[", ""));
                            log.setMessage(split[3].replace("]", "").replace("[", ""));
                            logService.saveOrUpdate(log);
                        }
                        lastTimeFileSize = raf.length();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (raf != null) {
                            try {
                                raf.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            task.setLogLength(lastTimeFileSize);
            taskService.saveOrUpdate(task);
        }
    }
}


//class readLog implements Runnable {
//    private File logFile;
//    private long lastTimeFileSize = 0;
//
//    public readLog(String filePath) {
//        this.logFile = new File(filePath);
//    }
//
//    @Override
//    public void run() {
//        long len = logFile.length();
//        RandomAccessFile raf = null;
//        if (len <= lastTimeFileSize) {
//            System.out.println(logFile + "日志并没有任何新的输入");
//        } else if (len > lastTimeFileSize) {
//            try {
//                raf = new RandomAccessFile(logFile, "r");
//                raf.seek(lastTimeFileSize+1);
//                String line = null;
//                while ((line = raf.readLine()) != null) {
//                    if("".equals(line)){
//                        continue;
//                    }
//                    System.out.println(logFile + "的数据:" + line);
//                }
//                lastTimeFileSize = raf.length();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (raf != null) {
//                    try {
//                        raf.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//}