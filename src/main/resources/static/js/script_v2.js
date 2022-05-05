const track = document.getElementById("track");
const thumbnail = document.getElementById("thumbnail");
const background = document.getElementById("background");
const trackArtist = document.getElementById("track-artist");
const trackTitle = document.getElementById("track-title");
const progressBar = document.getElementById("progressBar");
const currentTime = document.getElementById("currentTime");
const durationTime = document.getElementById("durationTime");

let play = document.getElementById("play");
let pause = document.getElementById("pause");
let next = document.getElementById("next-track");
let prev = document.getElementById("prev-track");
trackIndex = 0;

tracks = re();
trackArtists = getartists();
trackTitles = getTitles();
thumbnails = getIMG();


let playing = true;

function getartists() {
    trackArtists = [];
    document.getElementById('info').innerHTML = "";
    var myTab = document.getElementById('songTable');
    for (i = 1; i < myTab.rows.length; i++) {
        var objCells = myTab.rows.item(i).cells;
        var artist = objCells.item(4).textContent;
        console.log(artist);
        this.trackArtists[i - 1] = artist;
    }
    trackArtist.textContent = trackArtists[0];
    return trackArtists;
}

function getTitles() {
    trackTitles = [];
    document.getElementById('info').innerHTML = "";
    var myTab = document.getElementById('songTable');

    for (i = 1; i < myTab.rows.length; i++) {
        var objCells = myTab.rows.item(i).cells;
        var title = objCells.item(1).textContent;
        console.log(title);
        this.trackTitles[i - 1] = title;
    }
    trackTitle.textContent = trackTitles[0];
    return trackTitles;
}

function getIMG() {
    thumbnails = [];
    document.getElementById('info').innerHTML = "";
    var myTab = document.getElementById('songTable');
    for (i = 1; i < myTab.rows.length; i++) {
        if ((i - 1) % 2 == 0) {
            this.thumbnails[i - 1] = "https://i.ibb.co/7RhfRBZ/Fine-Line-Harry-Styles.jpg";
            console.log(this.thumbnails[i - 1].src);
        } else {
            this.thumbnails[i - 1] = "https://i.ibb.co/371t9Md/Loud-Luxury-Song-Cover-Art.jpg";
            console.log(i - 1);
        }
    }
    thumbnail.src = thumbnails[0];
    background.src = thumbnails[0];
    return thumbnails;
}

function re() {
    tracks = [];
    document.getElementById('info').innerHTML = "";
    var myTab = document.getElementById('songTable');
    var host = document.getElementById('host-s-s').value;
    console.log(host);
    for (i = 1; i < myTab.rows.length; i++) {
        var objCells = myTab.rows.item(i).cells;
        var one = objCells.item(0).textContent;
        tracks[i - 1] = "http://localhost:8098/songs/" + one;

    }
    track.src = tracks[0];
    return tracks;
}

function playSongByID(element) {
    var row = element.parentNode.parentNode;
    console.log(row.rowIndex);
    // b.playSongByID(row.rowIndex-1);
    trackIndex = row.rowIndex;

    track.src = tracks[row.rowIndex - 1];
    thumbnail.src = thumbnails[row.rowIndex - 1];
    background.src = thumbnails[row.rowIndex - 1];

    trackArtist.textContent = trackArtists[row.rowIndex - 1];
    trackTitle.textContent = trackTitles[row.rowIndex - 1];

    playing = true;
    pausePlay();
}

function pausePlay() {
    if (playing) {
        play.style.display = "none";
        pause.style.display = "block";

        thumbnail.style.transform = "scale(1.25)";

        track.play();
        playing = false;
    } else {
        pause.style.display = "none";
        play.style.display = "block";

        thumbnail.style.transform = "scale(1)";

        track.pause();
        playing = true;
    }
}

play.addEventListener("click", pausePlay);
pause.addEventListener("click", pausePlay);

track.addEventListener("ended", nextTrack);

function nextTrack() {
    trackIndex++;
    if (trackIndex > tracks.length - 1) {
        trackIndex = 0;
    }

    track.src = tracks[trackIndex];
    thumbnail.src = thumbnails[trackIndex];
    background.src = thumbnails[trackIndex];

    trackArtist.textContent = trackArtists[trackIndex];
    trackTitle.textContent = trackTitles[trackIndex];

    playing = true;
    pausePlay();
}

next.addEventListener("click", nextTrack);

function prevTrack() {
    trackIndex--;
    if (trackIndex < 0) {
        trackIndex = tracks.length - 1;
    }

    track.src = tracks[trackIndex];
    thumbnail.src = thumbnails[trackIndex];
    background.src = thumbnails[trackIndex];

    trackArtist.textContent = trackArtists[trackIndex];
    trackTitle.textContent = trackTitles[trackIndex];

    playing = true;
    pausePlay();
}

prev.addEventListener("click", prevTrack);

function progressValue() {
    progressBar.max = track.duration;
    progressBar.value = track.currentTime;

    currentTime.textContent = formatTime(track.currentTime);
    durationTime.textContent = formatTime(track.duration);
}

setInterval(progressValue, 500);

function formatTime(sec) {
    let minutes = Math.floor(sec / 60);
    let seconds = Math.floor(sec - minutes * 60);
    if (seconds < 10) {
        seconds = `0${seconds}`;
    }
    return `${minutes}:${seconds}`;
}

function changeProgressBar() {
    track.currentTime = progressBar.value;
}

progressBar.addEventListener("click", changeProgressBar);
