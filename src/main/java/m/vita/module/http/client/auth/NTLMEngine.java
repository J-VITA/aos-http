package m.vita.module.http.client.auth;

public interface NTLMEngine {

    /**
     * Generates a Type1 message given the domain and workstation.
     *
     * @param domain Optional Windows domain name. Can be <code>null</code>.
     * @param workstation Optional Windows workstation name. Can be
     *  <code>null</code>.
     * @return Type1 message
     * @throws NTLMEngineException
     */
    String generateType1Msg(
            String domain,
            String workstation) throws NTLMEngineException;

    /**
     * Generates a Type3 message given the user credentials and the
     * authentication challenge.
     *
     * @param username Windows user name
     * @param password Password
     * @param domain Windows domain name
     * @param workstation Windows workstation name
     * @param challenge Type2 challenge.
     * @return Type3 response.
     * @throws NTLMEngineException
     */
    String generateType3Msg(
            String username,
            String password,
            String domain,
            String workstation,
            String challenge) throws NTLMEngineException;

}
