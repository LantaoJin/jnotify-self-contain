package net.contentobjects.jnotify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class Util
{
    private static final String DYNAMIC_LIBRARY_DIR = "/dependences/";
    private static final String RUNTIME_DYNAMIC_LIBRARY_DIR = File.separator + "native" + File.separator;
    
    public static String getMaskDesc(int mask)
    {
        StringBuffer s = new StringBuffer();
        if ((mask & JNotify.FILE_CREATED) != 0)
        {
            s.append("FILE_CREATED|");
        }
        if ((mask & JNotify.FILE_DELETED) != 0)
        {
            s.append("FILE_DELETED|");
        }
        if ((mask & JNotify.FILE_MODIFIED) != 0)
        {
            s.append("FILE_MODIFIED|");
        }
        if ((mask & JNotify.FILE_RENAMED) != 0)
        {
            s.append("FILE_RENAMED|");
        }
        if (s.length() > 0)
        {
            return s.substring(0, s.length() - 1);
        }
        else
        {
            return "UNKNOWN";
        }
    }
    
    /**
     * JNotify work together with a native library file. If we put the jnotify.jar to private maven repository,
     * we will download and set runtime library manually, judge system environment by ourselves.
     * If our application environment beyond our control, we couldn't run it well.
     * The main modifications are below:
     * I packaged the native library files into jnotify.jar.
     * When the relevant class is loading, it will find the library file in jar and write to
     * application "libs" direction. After that, load it.
     * @param clazz
     * @param libFilename
     * @throws URISyntaxException
     * @throws IOException
     * @author Alan Jin
     */
    public static <T> void loadLibraray(Class<T> clazz, String libFilename) throws URISyntaxException, IOException
    {
        InputStream is = null;
        FileOutputStream fos = null;
        try
        {
            is = clazz.getResourceAsStream(DYNAMIC_LIBRARY_DIR + libFilename);
            File currentJarFile = new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
            File runtimeLibFile;
            runtimeLibFile = new File(currentJarFile.getParentFile().getAbsolutePath() + RUNTIME_DYNAMIC_LIBRARY_DIR + libFilename);
            if (runtimeLibFile.exists())
            {
                runtimeLibFile.delete();
            }
            File parent = runtimeLibFile.getParentFile(); 
            if (parent != null && !parent.exists())
            { 
                parent.mkdirs(); 
            } 
            fos = new FileOutputStream(runtimeLibFile);
            int n = -1;
            do
            {
                if ((n = is.read()) != -1)
                {
                    fos.write(n);
                }
            } while (n != -1);
            fos.close();
            fos = null;
            is.close();
            is = null;
            System.load(runtimeLibFile.getAbsolutePath());
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                    fos = null;
                }
                catch (IOException e)
                {
                }
            }
            if (is != null)
            {
                try
                {
                    is.close();
                    is = null;
                }
                catch (IOException e)
                {
                }
            }
        }
    }
}
