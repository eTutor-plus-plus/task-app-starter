package at.jku.dke.etutor.task_app.auth;

/**
 * Specifies some authentication/authorization constants.
 */
public final class AuthConstants {
    private AuthConstants() {
    }

    /**
     * The header name of the authentication token.
     */
    public static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";

    /**
     * Clients with {@code CRUD} permission are allowed to manage tasks and task groups
     * as well as submit solutions.
     */
    public static final String CRUD = "CRUD";

    /**
     * Clients with {@code SUBMIT} authority are allowed to submit solutions.
     */
    public static final String SUBMIT = "SUBMIT";

    /**
     * Clients with {@code READ_SUBMISSION} permission are allowed to read submissions.
     */
    public static final String READ_SUBMISSION = "READ_SUBMISSION";

    //#region --- Authorities ---
    /**
     * The CRUD authority.
     *
     * @see #CRUD
     */
    public static final String CRUD_AUTHORITY = "hasAuthority('" + CRUD + "')";

    /**
     * The SUBMIT authority.
     *
     * @see #SUBMIT
     */
    public static final String SUBMIT_AUTHORITY = "hasAuthority('" + SUBMIT + "')";

    /**
     * The READ_SUBMISSION authority.
     *
     * @see #READ_SUBMISSION
     */
    public static final String READ_SUBMISSION_AUTHORITY = "hasAuthority('" + READ_SUBMISSION + "')";
    //#endregion
}
