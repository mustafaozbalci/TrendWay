package commercial.TrendWay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseModel {
    private Integer status;
    private String message;
    private Object data;
}
