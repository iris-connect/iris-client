package iris.client_bff.vaccination_info;

/**
 * @author Jens Kutzsche
 */
public class VaccinationInfoAnnouncementException extends RuntimeException {

	private static final long serialVersionUID = -62922796972802753L;

	public VaccinationInfoAnnouncementException(String msg, Throwable e) {
		super(msg, e);
	}
}
