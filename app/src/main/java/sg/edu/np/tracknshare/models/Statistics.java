package sg.edu.np.tracknshare.models;

public class Statistics {
    public String Title;
    public static String Stat1Header = "Daily Average";
    public static String Stat2Header = "Total";
    public int Stat1number;
    public int Stat2number;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getStat1Header() {
        return Stat1Header;
    }

    public void setStat1Header(String stat1Header) {
        Stat1Header = stat1Header;
    }

    public String getStat2Header() {
        return Stat2Header;
    }

    public void setStat2Header(String stat2Header) {
        Stat2Header = stat2Header;
    }

    public int getStat1number() {
        return Stat1number;
    }

    public void setStat1number(int stat1number) {
        Stat1number = stat1number;
    }

    public int getStat2number() {
        return Stat2number;
    }

    public void setStat2number(int stat2number) {
        Stat2number = stat2number;
    }
}
