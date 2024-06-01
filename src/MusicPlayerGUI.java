import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;

public class MusicPlayerGUI extends JFrame {

    public static final Color FRAME_COLOR = new Color(64, 159, 159);
    public static final Color TEXT_COLOR = new Color(237, 237, 237);

    private MusicPlayer musicPlayer;

    private JFileChooser jFileChooser;

    private JLabel songTitle;
    private JLabel songArtist;

    private JPanel playbackButtons;

    private JSlider playbackSlider;



    public MusicPlayerGUI(){
        setTitle("Music Player");

        setSize(400,600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(FRAME_COLOR);
        ImageIcon icon = new ImageIcon("Assets/images/music.png");
        setIconImage(icon.getImage());



        musicPlayer = new MusicPlayer(this);

        jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("C:\\Users\\George ongoro\\Pictures\\Music"));
        jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));

        addGuiComponents();
    }

    private void addGuiComponents(){
        addToolbar();

        //SONG ICON
        JLabel songImage = new JLabel(loadImageIcon("Assets/images/music_note_blue.png"));
        songImage.setBounds(2, 50, getWidth() - 20, 225);
        add(songImage);

        //SONG TITLE
        songTitle = new JLabel("Song Title");
        songTitle.setBounds(0,285,getWidth() - 10, 30);
        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
        songTitle.setFont(new Font("Segoe UI", Font.BOLD,24));
        songTitle.setForeground(TEXT_COLOR);
        add(songTitle);

        //SONG ARTIST
        songArtist = new JLabel("Song Artist");
        songArtist.setBounds(0,315,getWidth() - 10, 30);
        songArtist.setHorizontalAlignment(SwingConstants.CENTER);
        songArtist.setFont(new Font("Segoe UI", Font.PLAIN,24));
        songArtist.setForeground(TEXT_COLOR);
        add(songArtist);

        //PLAYBACK SLIDER
        playbackSlider = new JSlider(JSlider.HORIZONTAL, 0,100,0);
        playbackSlider.setBounds(getWidth()/2 - 300/2,365,300,40);
        playbackSlider.setBackground(null);
        playbackSlider.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

                musicPlayer.pauseSong();

            }

            @Override
            public void mouseReleased(MouseEvent e) {

                JSlider source = (JSlider) e.getSource();

                int frame = source.getValue();

                musicPlayer.setCurrentFrame(frame);

                musicPlayer.setCurrentTimeInMilliseconds((int) (frame / (2.02 * musicPlayer.getCurrentSong().getFrameRatePerMillisecond())));

                //resume the song
                musicPlayer.playCurrentSong();
                disablePlayButton();

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });
        add(playbackSlider);

        //PLAYBACK BUTTONS
        addPlaybackButtons();
    }

    private void addToolbar(){

        JToolBar toolBar = new JToolBar();
        toolBar.setBounds(0,0,getWidth(),20);

        toolBar.setFloatable(false);

        JMenuBar menuBar = new JMenuBar();
        toolBar.add(menuBar);

        JMenu songMenu = new JMenu("Song");
        menuBar.add(songMenu);

        JMenuItem loadSong = new JMenuItem("Load Song");
        loadSong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = jFileChooser.showOpenDialog(MusicPlayerGUI.this);
                File selectedFile = jFileChooser.getSelectedFile();

                if(result == JFileChooser.APPROVE_OPTION && selectedFile != null){
                    Song song = new Song(selectedFile.getPath());

                    musicPlayer.loadSong(song);

                    updateSongDetails(song);
                    updatePlaybackSlider(song);
                    disablePlayButton();
                }
            }
        });
        songMenu.add(loadSong);

        JMenu playlistMenu = new JMenu("Playlist");
        menuBar.add(playlistMenu);

        JMenuItem createPlaylist = new JMenuItem("Create Playlist");
        createPlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Load music playlist dialog
                new MusicPlaylistDialog(MusicPlayerGUI.this).setVisible(true);

            }
        });
        playlistMenu.add(createPlaylist);

        JMenuItem loadPlaylist = new JMenuItem("Load Playlist");
        loadPlaylist.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser1 = new JFileChooser();
                jFileChooser1.setFileFilter(new FileNameExtensionFilter("Playlist", "txt"));
                jFileChooser1.setCurrentDirectory(new File("Assets/mscFiles"));

                int result = jFileChooser1.showOpenDialog(MusicPlayerGUI.this);
                File selectedFile = jFileChooser1.getSelectedFile();

                if(result ==JFileChooser.APPROVE_OPTION && selectedFile != null){
                    musicPlayer.stopSong();

                    musicPlayer.loadPlaylist(selectedFile);
                }

            }
        });
        playlistMenu.add(loadPlaylist);

        JMenu aboutMenu = new JMenu("About");
        JMenuItem aboutItem = new JMenuItem("About Player");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MusicPlayerGUI.this, "" +
                        "Name: GelPlayer\n" +
                        "Version: 1.0.190424\n" +
                        "Author: GEORGE ONGORO\n" +
                        "\n" +
                        "THIS IS A FREE WARE APPLICATION.\n REPORT BUGS AND ISSUES IN 'gtechong72@gmail.com'.\n THANK YOU FOR USING OUR APPLICATION");
            }
        });
        aboutMenu.add(aboutItem);





        ///END-USER AGREEMENT ===================

        JMenuItem licence = new JMenuItem("End-user agreement");
        licence.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MusicPlayerGUI.this, "\n" +
                        "\n" +
                        "   TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION\n" +
                        "\n" +
                        "   1. Definitions.\n" +
                        "\n" +
                        "      \"License\" shall mean the terms and conditions for use, reproduction,\n" +
                        "      and distribution as defined by Sections 1 through 9 of this document.\n" +
                        "\n" +
                        "      \"Licensor\" shall mean the copyright owner or entity authorized by\n" +
                        "      the copyright owner that is granting the License.\n" +
                        "\n" +
                        "      \"Legal Entity\" shall mean the union of the acting entity and all\n" +
                        "      other entities that control, are controlled by, or are under common\n" +
                        "      control with that entity. For the purposes of this definition,\n" +
                        "      \"control\" means (i) the power, direct or indirect, to cause the\n" +
                        "      direction or management of such entity, whether by contract or\n" +
                        "      otherwise, or (ii) ownership of fifty percent (50%) or more of the\n" +
                        "      outstanding shares, or (iii) beneficial ownership of such entity.\n" +
                        "\n" +
                        "      \"You\" (or \"Your\") shall mean an individual or Legal Entity\n" +
                        "      exercising permissions granted by this License.\n" +
                        "\n" +
                        "      \"Source\" form shall mean the preferred form for making modifications,\n" +
                        "      including but not limited to software source code, documentation\n" +
                        "      source, and configuration files.\n" +
                        "\n" +
                        "      \"Object\" form shall mean any form resulting from mechanical\n" +
                        "      transformation or translation of a Source form, including but\n" +
                        "      not limited to compiled object code, generated documentation,\n" +
                        "      and conversions to other media types.\n" +
                        "\n" +
                        "      \"Work\" shall mean the work of authorship, whether in Source or\n" +
                        "      Object form, made available under the License, as indicated by a\n" +
                        "      copyright notice that is included in or attached to the work\n" +
                        "      (an example is provided in the Appendix below).\n" +
                        "\n" +
                        "      \"Derivative Works\" shall mean any work, whether in Source or Object\n" +
                        "      form, that is based on (or derived from) the Work and for which the\n" +
                        "      editorial revisions, annotations, elaborations, or other modifications\n" +
                        "      represent, as a whole, an original work of authorship. For the purposes\n" +
                        "      of this License, Derivative Works shall not include works that remain\n" +
                        "      separable from, or merely link (or bind by name) to the interfaces of,\n" +
                        "      the Work and Derivative Works thereof.\n" +
                        "\n" +
                        "      \"Contribution\" shall mean any work of authorship, including\n" +
                        "      the original version of the Work and any modifications or additions\n" +
                        "      to that Work or Derivative Works thereof, that is intentionally\n" +
                        "      submitted to Licensor for inclusion in the Work by the copyright owner\n" +
                        "      or by an individual or Legal Entity authorized to submit on behalf of\n" +
                        "      the copyright owner. For the purposes of this definition, \"submitted\"\n" +
                        "      means any form of electronic, verbal, or written communication sent\n" +
                        "      to the Licensor or its representatives, including but not limited to\n" +
                        "      communication on electronic mailing lists, source code control systems,\n" +
                        "      and issue tracking systems that are managed by, or on behalf of, the\n" +
                        "      Licensor for the purpose of discussing and improving the Work, but\n" +
                        "      excluding communication that is conspicuously marked or otherwise\n" +
                        "      designated in writing by the copyright owner as \"Not a Contribution.\"\n" +
                        "\n" +
                        "      \"Contributor\" shall mean Licensor and any individual or Legal Entity\n" +
                        "      on behalf of whom a Contribution has been received by Licensor and\n" +
                        "      subsequently incorporated within the Work.\n" +
                        "\n" +
                        "   2. Grant of Copyright License. Subject to the terms and conditions of\n" +
                        "      this License, each Contributor hereby grants to You a perpetual,\n" +
                        "      worldwide, non-exclusive, no-charge, royalty-free, irrevocable\n" +
                        "      copyright license to reproduce, prepare Derivative Works of,\n" +
                        "      publicly display, publicly perform, sublicense, and distribute the\n" +
                        "      Work and such Derivative Works in Source or Object form.\n" +
                        "\n" +
                        "   3. Grant of Patent License. Subject to the terms and conditions of\n" +
                        "      this License, each Contributor hereby grants to You a perpetual,\n" +
                        "      worldwide, non-exclusive, no-charge, royalty-free, irrevocable\n" +
                        "      (except as stated in this section) patent license to make, have made,\n" +
                        "      use, offer to sell, sell, import, and otherwise transfer the Work,\n" +
                        "      where such license applies only to those patent claims licensable\n" +
                        "      by such Contributor that are necessarily infringed by their\n" +
                        "      Contribution(s) alone or by combination of their Contribution(s)\n" +
                        "      with the Work to which such Contribution(s) was submitted. If You\n" +
                        "      institute patent litigation against any entity (including a\n" +
                        "      cross-claim or counterclaim in a lawsuit) alleging that the Work\n" +
                        "      or a Contribution incorporated within the Work constitutes direct\n" +
                        "      or contributory patent infringement, then any patent licenses\n" +
                        "      granted to You under this License for that Work shall terminate\n" +
                        "      as of the date such litigation is filed.\n" +
                        "\n" +
                        "   4. Redistribution. You may reproduce and distribute copies of the\n" +
                        "      Work or Derivative Works thereof in any medium, with or without\n" +
                        "      modifications, and in Source or Object form, provided that You\n" +
                        "      meet the following conditions:\n" +
                        "\n" +
                        "      (a) You must give any other recipients of the Work or\n" +
                        "          Derivative Works a copy of this License; and\n" +
                        "\n" +
                        "      (b) You must cause any modified files to carry prominent notices\n" +
                        "          stating that You changed the files; and\n" +
                        "\n" +
                        "      (c) You must retain, in the Source form of any Derivative Works\n" +
                        "          that You distribute, all copyright, patent, trademark, and\n" +
                        "          attribution notices from the Source form of the Work,\n" +
                        "          excluding those notices that do not pertain to any part of\n" +
                        "          the Derivative Works; and\n" +
                        "\n" +
                        "      (d) If the Work includes a \"NOTICE\" text file as part of its\n" +
                        "          distribution, then any Derivative Works that You distribute must\n" +
                        "          include a readable copy of the attribution notices contained\n" +
                        "          within such NOTICE file, excluding those notices that do not\n" +
                        "          pertain to any part of the Derivative Works, in at least one\n" +
                        "          of the following places: within a NOTICE text file distributed\n" +
                        "          as part of the Derivative Works; within the Source form or\n" +
                        "          documentation, if provided along with the Derivative Works; or,\n" +
                        "          within a display generated by the Derivative Works, if and\n" +
                        "          wherever such third-party notices normally appear. The contents\n" +
                        "          of the NOTICE file are for informational purposes only and\n" +
                        "          do not modify the License. You may add Your own attribution\n" +
                        "          notices within Derivative Works that You distribute, alongside\n" +
                        "          or as an addendum to the NOTICE text from the Work, provided\n" +
                        "          that such additional attribution notices cannot be construed\n" +
                        "          as modifying the License.\n" +
                        "\n" +
                        "      You may add Your own copyright statement to Your modifications and\n" +
                        "      may provide additional or different license terms and conditions\n" +
                        "      for use, reproduction, or distribution of Your modifications, or\n" +
                        "      for any such Derivative Works as a whole, provided Your use,\n" +
                        "      reproduction, and distribution of the Work otherwise complies with\n" +
                        "      the conditions stated in this License.\n" +
                        "\n" +
                        "   5. Submission of Contributions. Unless You explicitly state otherwise,\n" +
                        "      any Contribution intentionally submitted for inclusion in the Work\n" +
                        "      by You to the Licensor shall be under the terms and conditions of\n" +
                        "      this License, without any additional terms or conditions.\n" +
                        "      Notwithstanding the above, nothing herein shall supersede or modify\n" +
                        "      the terms of any separate license agreement you may have executed\n" +
                        "      with Licensor regarding such Contributions.\n" +
                        "\n" +
                        "   6. Trademarks. This License does not grant permission to use the trade\n" +
                        "      names, trademarks, service marks, or product names of the Licensor,\n" +
                        "      except as required for reasonable and customary use in describing the\n" +
                        "      origin of the Work and reproducing the content of the NOTICE file.\n" +
                        "\n" +
                        "   7. Disclaimer of Warranty. Unless required by applicable law or\n" +
                        "      agreed to in writing, Licensor provides the Work (and each\n" +
                        "      Contributor provides its Contributions) on an \"AS IS\" BASIS,\n" +
                        "      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or\n" +
                        "      implied, including, without limitation, any warranties or conditions\n" +
                        "      of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A\n" +
                        "      PARTICULAR PURPOSE. You are solely responsible for determining the\n" +
                        "      appropriateness of using or redistributing the Work and assume any\n" +
                        "      risks associated with Your exercise of permissions under this License.\n" +
                        "\n" +
                        "   8. Limitation of Liability. In no event and under no legal theory,\n" +
                        "      whether in tort (including negligence), contract, or otherwise,\n" +
                        "      unless required by applicable law (such as deliberate and grossly\n" +
                        "      negligent acts) or agreed to in writing, shall any Contributor be\n" +
                        "      liable to You for damages, including any direct, indirect, special,\n" +
                        "      incidental, or consequential damages of any character arising as a\n" +
                        "      result of this License or out of the use or inability to use the\n" +
                        "      Work (including but not limited to damages for loss of goodwill,\n" +
                        "      work stoppage, computer failure or malfunction, or any and all\n" +
                        "      other commercial damages or losses), even if such Contributor\n" +
                        "      has been advised of the possibility of such damages.\n" +
                        "\n" +
                        "   9. Accepting Warranty or Additional Liability. While redistributing\n" +
                        "      the Work or Derivative Works thereof, You may choose to offer,\n" +
                        "      and charge a fee for, acceptance of support, warranty, indemnity,\n" +
                        "      or other liability obligations and/or rights consistent with this\n" +
                        "      License. However, in accepting such obligations, You may act only\n" +
                        "      on Your own behalf and on Your sole responsibility, not on behalf\n" +
                        "      of any other Contributor, and only if You agree to indemnify,\n" +
                        "      defend, and hold each Contributor harmless for any liability\n" +
                        "      incurred by, or claims asserted against, such Contributor by reason\n" +
                        "      of your accepting any such warranty or additional liability.\n" +
                        "\n" +
                        "   END OF TERMS AND CONDITIONS\n" +
                        "\n" +
                        "   APPENDIX: How to apply the Apache License to your work.\n" +
                        "\n" +
                        "      To apply the Apache License to your work, attach the following\n" +
                        "      boilerplate notice, with the fields enclosed by brackets \"[]\"\n" +
                        "      replaced with your own identifying information. (Don't include\n" +
                        "      the brackets!)  The text should be enclosed in the appropriate\n" +
                        "      comment syntax for the file format. We also recommend that a\n" +
                        "      file or class name and description of purpose be included on the\n" +
                        "      same \"printed page\" as the copyright notice for easier\n" +
                        "      identification within third-party archives.\n" +
                        "\n" +
                        "   Copyright [yyyy] [name of copyright owner]\n" +
                        "\n" +
                        "   Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "   you may not use this file except in compliance with the License.\n" +
                        "   You may obtain a copy of the License at\n" +
                        "\n" +
                        "   Unless required by applicable law or agreed to in writing, software\n" +
                        "   distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "   See the License for the specific language governing permissions and\n" +
                        "   limitations under the License.\n", "END-USER AGREEMENT", JOptionPane.PLAIN_MESSAGE);

                ///END-USER AGREEMENT ===================
            }
        });
        aboutMenu.add(licence);


        menuBar.add(aboutMenu);

        add(toolBar);
    }

    private ImageIcon loadImageIcon(String imagePath){
        try{
            BufferedImage image = ImageIO.read(new File(imagePath));

            return new ImageIcon(image);

        }catch (Exception e){
            e.printStackTrace();

            return null;
        }
    }

    private void addPlaybackButtons(){

        playbackButtons = new JPanel();
        playbackButtons.setBounds(2, 435,getWidth() - 10,80);
        playbackButtons.setBackground(null);

        //PREV BUTTON
        JButton prevButton = new JButton(loadImageIcon("Assets/images/prev2.png"));
        prevButton.setBorderPainted(false);
        prevButton.setBackground(null);
        prevButton.setFocusable(false);
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                musicPlayer.prevSong();
            }
        });

        playbackButtons.add(prevButton);


        //PLAY BUTTON
        JButton playButton = new JButton(loadImageIcon("Assets/images/play2.png"));
        playButton.setBorderPainted(false);
        playButton.setBackground(null);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disablePlayButton();
                musicPlayer.playCurrentSong();
            }
        });
        playbackButtons.add(playButton);
        playButton.setFocusable(false);

        //PAUSE BUTTON
        JButton pauseButton = new JButton(loadImageIcon("Assets/images/pause2.png"));
        pauseButton.setBorderPainted(false);
        pauseButton.setBackground(null);
        pauseButton.setVisible(false);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enablePlayButton();
                musicPlayer.pauseSong();

            }
        });
        playbackButtons.add(pauseButton);
        pauseButton.setFocusable(false);

        //NEXT BUTTON
        JButton nextButton = new JButton(loadImageIcon("Assets/images/next2.png"));
        nextButton.setBorderPainted(false);
        nextButton.setBackground(null);
        nextButton.setFocusable(false);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                musicPlayer.nextSong();
            }
        });

        playbackButtons.add(nextButton);


        add(playbackButtons);

    }

    public void setPlaybackSliderValue(int frame){
        playbackSlider.setValue(frame);
    }

    public void updateSongDetails(Song song){
        songTitle.setText(song.getSongTitle());
        songArtist.setText(song.getSongArtist());

        if(song.getSongTitle() == null && song.getSongArtist() == null){

            JOptionPane.showMessageDialog(MusicPlayerGUI.this, "Sorry! \n " +
                    "Cannot find Song title and Artist for the current song!!" +
                    "There must be an error on our end.");

        }
    }

    public void updatePlaybackSlider(Song song){

        playbackSlider.setMaximum(song.getMp3File().getFrameCount());

        Hashtable<Integer, JLabel> labelHashtable = new Hashtable<>();
        JLabel labelBeginning = new JLabel("00:00");
        labelBeginning.setFont(new Font("Segoe UI", Font.BOLD,10));
        labelBeginning.setForeground(TEXT_COLOR);

        JLabel labelEnd = new JLabel(song.getSongLength());
        labelEnd.setFont(new Font("Segoe UI", Font.BOLD,10));
        labelEnd.setForeground(TEXT_COLOR);

        labelHashtable.put(0, labelBeginning);
        labelHashtable.put(song.getMp3File().getFrameCount(),labelEnd);

        playbackSlider.setLabelTable(labelHashtable);
        playbackSlider.setPaintLabels(true);

    }

    public void disablePlayButton(){

        JButton playButton = (JButton) playbackButtons.getComponent(1);
        JButton pauseButton = (JButton) playbackButtons.getComponent(2);

        //TURN OFF PLAY BUTTON
        playButton.setVisible(false);
        playButton.setEnabled(false);

        //TURN ON PAUSE BUTTON
        pauseButton.setVisible(true);
        pauseButton.setEnabled(true);

    }
    
    public void enablePlayButton(){

        JButton playButton = (JButton) playbackButtons.getComponent(1);
        JButton pauseButton = (JButton) playbackButtons.getComponent(2);

        //TURN ON PLAY BUTTON
        playButton.setVisible(true);
        playButton.setEnabled(true);

        //TURN OFF PAUSE BUTTON
        pauseButton.setVisible(false);
        pauseButton.setEnabled(false);

    }

}






