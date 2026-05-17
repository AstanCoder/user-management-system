package com.example.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

class HexagonalArchitectureTest {

    @Test
    void contactDomainMustNotDependOnSpringOrJpa() {
        JavaClasses imported = new ClassFileImporter().importPackages("com.example.contact");
        ArchRule rule = noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("org.springframework..", "jakarta.persistence..");
        rule.check(imported);
    }

    @Test
    void identityDomainMustNotDependOnSpringOrJpa() {
        JavaClasses imported = new ClassFileImporter().importPackages("com.example.identity");
        ArchRule rule = noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("org.springframework..", "jakarta.persistence..");
        rule.check(imported);
    }

    @Test
    void userDomainMustNotDependOnSpringOrJpa() {
        JavaClasses imported = new ClassFileImporter().importPackages("com.example.user");
        ArchRule rule = noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("org.springframework..", "jakarta.persistence..");
        rule.check(imported);
    }
}
