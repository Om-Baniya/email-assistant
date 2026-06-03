import React from "react";
import axios from "axios";  
import {
  Container,
  Typography,
  TextField,
  Button,
  Box,
  FormControl,
  Select,
  MenuItem,
  CircularProgress,
  InputLabel,
} from "@mui/material";
import "./App.css";

function App() {
  const [emailContent, setEmailContent] = React.useState("");
  const [tone, setTone] = React.useState("");
  const [replyContent, setReplyContent] = React.useState("");
  const [loading, setLoading] = React.useState(false);
  const handleSubmit = async () => {
    setLoading(true);
    try {
      const response = await axios.post("http://localhost:8080/api/v1/email/generate", {
        emailContent,
        tone
      });
      setReplyContent(typeof response.data==="string"?response.data:JSON.stringify(response.data));
    } catch (error) {
      console.error("Error generating reply:", error);
    } finally {
      setLoading(false);
    }
  };
  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Typography variant="h3" component="h1" gutterBottom>
        Email Reply Generator
      </Typography>
      <Box sx={{ mx: 4 }}>
        <TextField
          label="Original Email Content"
          multiline
          rows={6}
          variant="outlined"
          fullWidth
          value={emailContent || ""}
          onChange={(e) => setEmailContent(e.target.value)}
          sx={{ mb: 2 }}
        />
        <FormControl fullWidth sx={{ mb: 2 }}>
          <InputLabel >Tone(Optional)</InputLabel>
          <Select
            labelId="demo-simple-select-label"
            id="demo-simple-select"
            value={tone}
            label="Tone(Optional)"
            onChange={e => setTone(e.target.value)}
          >
            <MenuItem value={10}>None</MenuItem>
            <MenuItem value={20}>Formal</MenuItem>
            <MenuItem value={30}>Friendly</MenuItem>
            <MenuItem value={40}>Casual</MenuItem>
          </Select>
        </FormControl>
        <Button variant="contained" disabled={!emailContent || loading} color="primary" sx={{ mb: 2 }} onClick={handleSubmit}>
          {loading ? <CircularProgress size={24} /> : "Generate Reply"}
        </Button>
      </Box>
      <Box sx={{ mx: 3 }}>
        <TextField
          multiline
          rows={6}
          variant="outlined"
          fullWidth
          value={replyContent || ""}
          inputProps={{ readOnly: true }}
          sx={{ mb: 2 }}
        />
        <Button variant="outlined" color="primary" sx={{ mt: 2 }} onClick={() => navigator.clipboard.write(replyContent)}>
          Copy to Clipboard
        </Button>
      </Box>
    </Container>
  );
}

export default App;
