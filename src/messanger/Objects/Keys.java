package messanger.Objects;

import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Slavik on 04.12.2016.
 */
public class Keys {
    private String friedsName;
    private SecretKeySpec secretKeySpec;

    public String getFriedsName() {
        return friedsName;
    }

    public void setFriedsName(String friedsName) {
        this.friedsName = friedsName;
    }

    public SecretKeySpec getSecretKeySpec() {
        return secretKeySpec;
    }

    public void setSecretKeySpec(SecretKeySpec secretKeySpec) {
        this.secretKeySpec = secretKeySpec;
    }

}
