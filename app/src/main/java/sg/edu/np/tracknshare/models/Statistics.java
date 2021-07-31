package sg.edu.np.tracknshare.models;

public class Statistics {
    public String Title;
    public String Stat1Header = "Average";
    public String Stat2Header = "Total";
    public String Stat1number;
    public String Stat2number;

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

    public String getStat1number() {
        return Stat1number;
    }

    public void setStat1number(String stat1number) {
        Stat1number = stat1number;
    }

    public String getStat2number() {
        return Stat2number;
    }

    public void setStat2number(String stat2number) {
        Stat2number = stat2number;
    }

    public Statistics(String title, String stat1number, String stat2number) {
        Title = title;
        Stat1number = stat1number;
        Stat2number = stat2number;
    }
}
