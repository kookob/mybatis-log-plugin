package mybatis.log;

/**
 * 配置项参数
 *
 * @author ob
 */
public class ConfigVo {
    public boolean running = false;
    public boolean sqlFormat = false;
    public int indexNum = 1;

    public boolean getRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean getSqlFormat() {
        return sqlFormat;
    }

    public void setSqlFormat(boolean sqlFormat) {
        this.sqlFormat = sqlFormat;
    }

    public int getIndexNum() {
        return indexNum;
    }

    public void setIndexNum(int indexNum) {
        this.indexNum = indexNum;
    }
}
