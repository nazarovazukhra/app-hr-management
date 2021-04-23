package uz.pdp.apphrmanagement.entity.enums;

public enum TaskStatus {

    NEW(1),
    IN_PROGRESS(2),
    DONE(3);

    Integer id;
    TaskStatus() {
    }

    TaskStatus(Integer id) {
        this.id = id;
    }
}
