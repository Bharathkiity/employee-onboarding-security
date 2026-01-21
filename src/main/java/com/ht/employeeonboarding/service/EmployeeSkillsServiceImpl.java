package com.ht.employeeonboarding.service;

import com.ht.employeeonboarding.entity.Employee;
import com.ht.employeeonboarding.entity.EmployeeSkills;
import com.ht.employeeonboarding.repository.EmployeeRepository;
import com.ht.employeeonboarding.repository.EmployeeSkillsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeSkillsServiceImpl implements EmployeeSkillsServiceI {
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private EmployeeSkillsRepository employeeSkillsRepository;

    @Override
    public EmployeeSkills addSkillToEmployee(Long employeeId, EmployeeSkills skill) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        skill.setEmployee(employee);  // Link skill to employee
        return employeeSkillsRepository.save(skill);
    }

    @Override
    public List<EmployeeSkills> getSkillsByEmployeeId(Long employeeId) {
        return employeeSkillsRepository.findByEmployeeId(employeeId);
    }

    @Override
    public Optional<EmployeeSkills> getSkillById(Long employeeId, Long skillId) {
        return employeeSkillsRepository.findById(skillId);  // Return Optional here
    }

    @Override
    public EmployeeSkills updateSkill(Long employeeId, Long skillId, EmployeeSkills updatedSkill) {
        Optional<EmployeeSkills> existingSkill = employeeSkillsRepository.findById(skillId);
        if (!existingSkill.isPresent()) {
            throw new RuntimeException("Skill not found with ID: " + skillId);
        }
        
        EmployeeSkills skill = existingSkill.get();
        skill.setCategory(updatedSkill.getCategory());
        skill.setSkills(updatedSkill.getSkills());
        skill.setExperience(updatedSkill.getExperience());
        
        return employeeSkillsRepository.save(skill);
    }

    @Override
    public void deleteSkill(Long employeeId, Long skillId) {
        employeeSkillsRepository.deleteById(skillId);
    }
}
