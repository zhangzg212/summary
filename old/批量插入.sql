<!-- 批量插入生成的兑换码 -->
 2      <insert id ="insertCodeBatch" parameterType="java.util.List" >
 3             <selectKey resultType ="java.lang.Integer" keyProperty= "id"
 4                  order= "AFTER">
 5                 SELECT LAST_INSERT_ID()
 6             </selectKey >
 7            insert into redeem_code
 8            (bach_id, code, type, facevalue,create_user,create_time)
 9            values
10             <foreach collection ="list" item="reddemCode" index= "index" separator =",">
11                 (
12                 #{reddemCode.batchId}, #{reddemCode.code},
13                 #{reddemCode.type},
14                 #{reddemCode.facevalue},
15                 #{reddemCode.createUser}, #{reddemCode.createTime}
16                 )
17             </foreach >
18      </insert >