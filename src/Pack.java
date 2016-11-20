import com.google.gson.annotations.SerializedName;

public class Pack {

    @SerializedName("b")
    private String botName;
    @SerializedName("n")
    private String packNumber;
    @SerializedName("f")
    private String fileName;
    @SerializedName("s")
    private String fileSize;

    public Pack() {

    }

    public Pack(String botName, String packNumber, String fileName, String fileSize) {
        this.botName = botName;
        this.packNumber = packNumber;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getPackNumber() {
        return packNumber;
    }

    public void setPackNumber(String packNumber) {
        this.packNumber = packNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

}
