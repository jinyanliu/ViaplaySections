
package se.sugarest.jane.viaplaysections.data.type;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Links {

    private List<Cury> curies = null;
    private Self self;
    private ViaplayRoot viaplayRoot;
    private ViaplayEditorial viaplayEditorial;
    private ViaplaySocket viaplaySocket;
    private ViaplaySocket2 viaplaySocket2;
    private ViaplaySearch viaplaySearch;
    private ViaplayAutocomplete viaplayAutocomplete;
    private ViaplayByGuid viaplayByGuid;
    private ViaplayValidateSession viaplayValidateSession;
    private ViaplayTranslations viaplayTranslations;
    private ViaplayTechnotifier viaplayTechnotifier;

    @SerializedName("viaplay:sections")
    private List<ViaplaySection> viaplaySections = null;
    private ViaplayLogin viaplayLogin;
    private ViaplayFacebookLogin viaplayFacebookLogin;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public List<Cury> getCuries() {
        return curies;
    }

    public void setCuries(List<Cury> curies) {
        this.curies = curies;
    }

    public Self getSelf() {
        return self;
    }

    public void setSelf(Self self) {
        this.self = self;
    }

    public ViaplayRoot getViaplayRoot() {
        return viaplayRoot;
    }

    public void setViaplayRoot(ViaplayRoot viaplayRoot) {
        this.viaplayRoot = viaplayRoot;
    }

    public ViaplayEditorial getViaplayEditorial() {
        return viaplayEditorial;
    }

    public void setViaplayEditorial(ViaplayEditorial viaplayEditorial) {
        this.viaplayEditorial = viaplayEditorial;
    }

    public ViaplaySocket getViaplaySocket() {
        return viaplaySocket;
    }

    public void setViaplaySocket(ViaplaySocket viaplaySocket) {
        this.viaplaySocket = viaplaySocket;
    }

    public ViaplaySocket2 getViaplaySocket2() {
        return viaplaySocket2;
    }

    public void setViaplaySocket2(ViaplaySocket2 viaplaySocket2) {
        this.viaplaySocket2 = viaplaySocket2;
    }

    public ViaplaySearch getViaplaySearch() {
        return viaplaySearch;
    }

    public void setViaplaySearch(ViaplaySearch viaplaySearch) {
        this.viaplaySearch = viaplaySearch;
    }

    public ViaplayAutocomplete getViaplayAutocomplete() {
        return viaplayAutocomplete;
    }

    public void setViaplayAutocomplete(ViaplayAutocomplete viaplayAutocomplete) {
        this.viaplayAutocomplete = viaplayAutocomplete;
    }

    public ViaplayByGuid getViaplayByGuid() {
        return viaplayByGuid;
    }

    public void setViaplayByGuid(ViaplayByGuid viaplayByGuid) {
        this.viaplayByGuid = viaplayByGuid;
    }

    public ViaplayValidateSession getViaplayValidateSession() {
        return viaplayValidateSession;
    }

    public void setViaplayValidateSession(ViaplayValidateSession viaplayValidateSession) {
        this.viaplayValidateSession = viaplayValidateSession;
    }

    public ViaplayTranslations getViaplayTranslations() {
        return viaplayTranslations;
    }

    public void setViaplayTranslations(ViaplayTranslations viaplayTranslations) {
        this.viaplayTranslations = viaplayTranslations;
    }

    public ViaplayTechnotifier getViaplayTechnotifier() {
        return viaplayTechnotifier;
    }

    public void setViaplayTechnotifier(ViaplayTechnotifier viaplayTechnotifier) {
        this.viaplayTechnotifier = viaplayTechnotifier;
    }

    public List<ViaplaySection> getViaplaySections() {
        return viaplaySections;
    }

    public void setViaplaySections(List<ViaplaySection> viaplaySections) {
        this.viaplaySections = viaplaySections;
    }

    public ViaplayLogin getViaplayLogin() {
        return viaplayLogin;
    }

    public void setViaplayLogin(ViaplayLogin viaplayLogin) {
        this.viaplayLogin = viaplayLogin;
    }

    public ViaplayFacebookLogin getViaplayFacebookLogin() {
        return viaplayFacebookLogin;
    }

    public void setViaplayFacebookLogin(ViaplayFacebookLogin viaplayFacebookLogin) {
        this.viaplayFacebookLogin = viaplayFacebookLogin;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
