package nextstep.courses.domain.course.session;

import nextstep.courses.domain.BaseEntity;
import nextstep.courses.domain.course.session.image.Image;
import nextstep.courses.domain.course.session.image.Images;
import nextstep.payments.domain.Payment;
import nextstep.users.domain.NsUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Session extends BaseEntity {
    private Long id;

    private Images images;

    private Duration duration;

    private SessionState sessionState;

    private Applicants applicants;

    private SessionStatus sessionStatus;

    public Session(Images images, Duration duration, SessionState sessionState,
                   Long creatorId, LocalDateTime date) {
        this(0L, images, duration, sessionState, new Applicants(),
                SessionStatus.READY, creatorId, date, null);
    }

    public Session(Long id, Images images, Duration duration, SessionState sessionState,
                   Applicants applicants, SessionStatus sessionStatus, Long creatorId,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(creatorId, createdAt, updatedAt);
        if (images == null) {
            throw new IllegalArgumentException("이미지를 추가해야 합니다");
        }

        if (duration == null) {
            throw new IllegalArgumentException("기간을 추가해야 합니다.");
        }

        if (sessionState == null) {
            throw new IllegalArgumentException("강의 상태를 추가해야 합니다.");
        }

        this.id = id;
        this.images = images;
        this.duration = duration;
        this.sessionState = sessionState;
        this.applicants = applicants;
        this.sessionStatus = sessionStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public void setSessionState(SessionState updateSessionState) {
        this.sessionState = updateSessionState;
    }

    public boolean sameAmount(Long amount) {
        return this.sessionState.sameAmount(amount);
    }

    public boolean sameId(Long sessionId) {
        return Objects.equals(this.id, sessionId);
    }

    public Long getId() {
        return this.id;
    }

    public int applyCount() {
        return this.applicants.size();
    }

    public Apply apply(NsUser loginUser, Payment payment, LocalDateTime date) {
        checkStatusOnRecruit();

        if (this.sessionState.charged()) {
            checkPaymentIsPaid(loginUser, payment);
        }

        this.applicants.addApplicant(loginUser, sessionState);
        return toApply(loginUser, date);
    }

    private Apply toApply(NsUser loginUser, LocalDateTime date) {
        return new Apply(this, loginUser, date);
    }

    private void checkStatusOnRecruit() {
        if (this.sessionStatus != SessionStatus.RECRUIT) {
            throw new IllegalArgumentException("강의 신청은 모집 중일 때만 가능 합니다.");
        }
    }

    private void checkPaymentIsPaid(NsUser loginUser, Payment payment) {
        if (payment == null || !payment.isPaid(loginUser, this)) {
            throw new IllegalArgumentException("결제를 진행해 주세요.");
        }
    }

    public void changeOnReady(LocalDate date) {
        checkStartDateIsSameOrBefore(date);
        this.sessionStatus = SessionStatus.READY;
    }

    public void changeOnRecruit(LocalDate date) {
        checkStartDateIsSameOrBefore(date);
        this.sessionStatus = SessionStatus.RECRUIT;
    }

    private void checkStartDateIsSameOrBefore(LocalDate date) {
        if (duration.startDateIsSameOrBefore(date)) {
            throw new IllegalArgumentException("강의 시작일 이전에 변경 가능합니다.");
        }
    }

    public void changeOnEnd(LocalDate date) {
        checkEndDateIsSameOrAfter(date);
        this.sessionStatus = SessionStatus.END;
    }

    private void checkEndDateIsSameOrAfter(LocalDate date) {
        if (duration.endDateIsSameOrAfter(date)) {
            throw new IllegalArgumentException("강의 종료일 이후 변경 가능합니다.");
        }
    }

    public Images getImages() {
        return images;
    }

    public Duration getDuration() {
        return duration;
    }

    public SessionState getSessionState() {
        return this.sessionState;
    }

    public Applicants getApplicants() {
        return applicants;
    }

    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", images=" + images +
                ", duration=" + duration +
                ", sessionState=" + sessionState +
                ", applicants=" + applicants +
                ", session=" + sessionStatus +
                '}';
    }
}
