package nextstep.courses.domain;

import nextstep.courses.domain.enums.SessionStatus;
import nextstep.courses.domain.enums.SessionType;
import nextstep.payments.domain.Payment;

abstract public class Session {
    protected final Long id;
    protected final Course course;
    protected final SessionDate sessionDate;
    protected SessionStatus sessionStatus;
    protected int numberOfStudents;
    protected CoverImageInfo coverImageInfo;
    protected final SessionType type;

    protected Session(Long id, Course course, SessionDate sessionDate, int numberOfStudents, CoverImageInfo coverImageInfo, SessionType type) {
        this(id, course, sessionDate, SessionStatus.READY, numberOfStudents, coverImageInfo, type);
    }

    protected Session(Long id, Course course, SessionDate sessionDate, SessionStatus sessionStatus, int numberOfStudents, CoverImageInfo coverImageInfo, SessionType type) {
        this.id = id;
        this.course = course;
        this.sessionDate = sessionDate;
        this.sessionStatus = sessionStatus;
        this.numberOfStudents = numberOfStudents;
        this.coverImageInfo = coverImageInfo;
        this.type = type;
    }

    abstract public void enroll(Payment payment);

    public void startRecruit() {
        sessionStatus = SessionStatus.RECRUITING;
    }

    public boolean hasNumberOfStudents(int targetCount) {
        return numberOfStudents == targetCount;
    }

    public Long getId() {return id;}

    public Course getCourse() {return course;}

    public SessionDate getSessionDate() {
        return sessionDate;
    }

    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public CoverImageInfo getCoverImageInfo() {
        return coverImageInfo;
    }

    public SessionType getType() {
        return type;
    }
}
