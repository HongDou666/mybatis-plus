package com.zqc.domain.po;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户详细信息（对应 user.info JSON 列）。
 * 字段与库中样例一致：{"age": 20, "intro": "佛系青年", "gender": "male"}
 * 无参构造供 Jackson/MP 反序列化；of(...) 便于测试与手工组装。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Schema(description = "用户详细信息")
public class UserInfo {

    @Schema(description = "年龄")
    private Integer age;

    @Schema(description = "简介")
    private String intro;

    @Schema(description = "性别（如 male / female）")
    private String gender;
}
