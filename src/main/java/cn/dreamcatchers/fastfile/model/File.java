package cn.dreamcatchers.fastfile.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by DeanTsui on 2018/9/5.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {
    private String id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime uploadTime;
}
