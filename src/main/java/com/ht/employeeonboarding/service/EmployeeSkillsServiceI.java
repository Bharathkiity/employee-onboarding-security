// EmployeeSkillsServiceI.java
package com.ht.employeeonboarding.service;

import java.util.List;
import java.util.Optional;

import com.ht.employeeonboarding.entity.EmployeeSkills;

public interface EmployeeSkillsServiceI {
    
    EmployeeSkills addSkillToEmployee(Long employeeId, EmployeeSkills skill);

    List<EmployeeSkills> getSkillsByEmployeeId(Long employeeId);

    Optional<EmployeeSkills> getSkillById(Long employeeId, Long skillId);  // Change this to Optional<EmployeeSkills>


    EmployeeSkills updateSkill(Long employeeId, Long skillId, EmployeeSkills updatedSkill);

    void deleteSkill(Long employeeId, Long skillId);
}
