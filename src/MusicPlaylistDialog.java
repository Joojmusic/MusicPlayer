import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MusicPlaylistDialog extends JDialog {

    private MusicPlayerGUI musicPlayerGUI;
    private ArrayList<String> songPaths;

    public MusicPlaylistDialog(MusicPlayerGUI musicPlayerGUI){
        songPaths = new ArrayList<>();
        this.musicPlayerGUI = musicPlayerGUI;

        //CONFIGURE DIALOGUE

        setTitle("Create PlayList");
        setSize(400,400);
        setResizable(false);
        getContentPane().setBackground(MusicPlayerGUI.FRAME_COLOR);
        setLayout(null);
        setModal(true);
        setLocationRelativeTo(musicPlayerGUI);

        addDialogueComponents();

    }

    private void addDialogueComponents() {
        JPanel songContainer = new JPanel();
        songContainer.setLayout(new BoxLayout(songContainer, BoxLayout.Y_AXIS));
        songContainer.setBounds((int)(getWidth() * 0.025), 10,(int)(getWidth() * 0.90), (int)(getHeight() * 0.75));
        add(songContainer);

        //ADD SONG BUTTON
        JButton addSongButton = new JButton("Add");
        addSongButton.setBounds(60, (int)(getHeight() * 0.80), 100,25);
        addSongButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addSongButton.setFocusable(false);
        addSongButton.setBackground(MusicPlayerGUI.FRAME_COLOR);
        addSongButton.setForeground(MusicPlayerGUI.TEXT_COLOR);
        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));
                jFileChooser.setCurrentDirectory(new File("Assets/mscFiles"));
                int result = jFileChooser.showOpenDialog(MusicPlaylistDialog.this);

                File selectedFile = jFileChooser.getSelectedFile();
                if(result == JFileChooser.APPROVE_OPTION && selectedFile != null){
                    JLabel filePathLabel = new JLabel(selectedFile.getPath());
                    filePathLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    filePathLabel.setBorder(BorderFactory.createLineBorder(MusicPlayerGUI.TEXT_COLOR));

                    //todo: add to the list
                    songPaths.add(filePathLabel.getText());

                    songContainer.add(filePathLabel);
                    songContainer.revalidate();
                }

            }
        });
        add(addSongButton);

        //SAVE PLAYLIST BUTTON
        JButton savePlaylistButton = new JButton("Save");
        savePlaylistButton.setBounds(220, (int)(getHeight() * 0.80), 100,25);
        savePlaylistButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        savePlaylistButton.setFocusable(false);
        savePlaylistButton.setBackground(MusicPlayerGUI.FRAME_COLOR);
        savePlaylistButton.setForeground(MusicPlayerGUI.TEXT_COLOR);
        savePlaylistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                    try {
                        JFileChooser jFileChooser = new JFileChooser();
                        jFileChooser.setCurrentDirectory(new File("Assets/mscFiles"));
                        int result = jFileChooser.showSaveDialog(MusicPlaylistDialog.this);

                        if(result == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = jFileChooser.getSelectedFile();

                            if (!selectedFile.getName().substring(selectedFile.getName().length() - 4).equalsIgnoreCase(".txt")) {
                                selectedFile = new File(selectedFile.getAbsoluteFile() + ".txt");
                            }

                            selectedFile.createNewFile();

                            FileWriter fileWriter = new FileWriter(selectedFile);
                            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

                            for (String songPath : songPaths) {
                                bufferedWriter.write(songPath + "\n");
                            }

                            bufferedWriter.close();

                            JOptionPane.showMessageDialog(MusicPlaylistDialog.this, "Successfully Created Playlist!");

                            MusicPlaylistDialog.this.dispose();

                        }

                    } catch (Exception ex) {

                        ex.printStackTrace();

                    }



            }
        });
        add(savePlaylistButton);

    }

}
