package org.bigmouth.nvwa.session.jedis;

import org.bigmouth.nvwa.session.Session;
import org.bigmouth.nvwa.session.SessionHolder;
import org.bigmouth.nvwa.session.Trackable;


public class JedisSessionHolder implements SessionHolder {

    @Override
    public Session get(Trackable trackable) {
        return null;
    }

    @Override
    public Session getSession(Trackable trackable) {
        return null;
    }

    @Override
    public void put(Session session) {
    }

    @Override
    public void remove(Trackable trackable) {
    }
}
