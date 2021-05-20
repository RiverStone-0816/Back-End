package kr.co.eicn.ippbx.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;

public class AESFileEncrypt256 {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = ALGORITHM + "/CBC/PKCS5Padding";
    private static final int AES_KEY_SIZE_256 = 256;
    private static final String INIT_VECTOR = "eicn_initial_vector";
    private static final String KEY_PADDING = "eicn_key_padding_sequential_string";

    private final Key key;
    private final IvParameterSpec ips;

    public AESFileEncrypt256(String keyStr) {
        byte[] iv = INIT_VECTOR.getBytes(StandardCharsets.UTF_8);
        //iv = Arrays.copyOf(iv, AES_KEY_SIZE_256 / 8);
        iv = Arrays.copyOf(iv, 16);
        this.ips = new IvParameterSpec(iv);
        this.key = generateKey(keyStr);
    }

    /**
     * <p>주어진 데이터로, AES 알고리즘에 사용할 비밀키(SecretKey)를 생성한다.</p>
     */
    public static Key generateKey(String keyStr) {
        SecretKeySpec keySpec = null;

        // Key size 맞춤 (256bit, 32byte)
        byte[] key = Arrays.copyOf((keyStr + KEY_PADDING).getBytes(StandardCharsets.UTF_8), AES_KEY_SIZE_256 / 8);
        keySpec = new SecretKeySpec(key, ALGORITHM);

        return keySpec;
    }


    /**
     * <p>
     * 원본 파일을 암호화해서 대상 파일을 만든다.
     * </p>
     *
     * @param srcName 원본 파일
     * @param dstName 대상 파일
     */
    public void encrypt(String srcName, String dstName) throws Exception {
        crypt(Cipher.ENCRYPT_MODE, srcName, dstName);
    }

    /**
     * <p>
     * 원본 파일을 복호화해서 대상 파일을 만든다.
     * </p>
     *
     * @param srcName 원본 파일
     * @param dstName 대상 파일
     */
    public void decrypt(String srcName, String dstName) throws Exception {
        crypt(Cipher.DECRYPT_MODE, srcName, dstName);
    }

    /**
     * <p>
     * 원본 파일을 암/복호화해서 대상 파일을 만든다.
     * </p>
     *
     * @param mode    암/복호화 모드
     * @param srcName 원본 파일
     * @param dstName 대상 파일
     */
    private void crypt(int mode, String srcName, String dstName) throws Exception {

        File source = new File(srcName);
        File dest = new File(dstName);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(mode, key, ips);

        try (InputStream input = new BufferedInputStream(new FileInputStream(source)); OutputStream output = new BufferedOutputStream(new FileOutputStream(dest))) {
            byte[] buffer = new byte[1024];
            int read = -1;
            while ((read = input.read(buffer)) != -1) {
                output.write(cipher.update(buffer, 0, read));
            }
            output.write(cipher.doFinal());
        }
    }

/*
	public static void main(String[] args) throws Exception {
		String test_key = "123456789abcdefg";

		AESFileEncrypt256 coder = new AESFileEncrypt256(test_key);
		coder.encrypt("C:/temp/asdf.txt"), "C:/temp/asdf_a.txt");
		coder.decrypt("C:/temp/asdf_a.txt"), "C:/temp/asdf_b.txt");
	}
*/

}
