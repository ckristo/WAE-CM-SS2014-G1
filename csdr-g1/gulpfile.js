// Gulp
var gulp = require('gulp'); 

// Plugins
var jshint = require('gulp-jshint');
var sass = require('gulp-ruby-sass');
var autoprefixer = require('gulp-autoprefixer');
var minifycss = require('gulp-minify-css');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var rename = require('gulp-rename');

// Lint
gulp.task('lint', function() {
    return gulp.src('public/javascripts/app/*.js')
        .pipe(jshint())
        .pipe(jshint.reporter('default'));
});

//Copy vanilla CSS files
gulp.task('css-copy', function() {
	return gulp.src('public/sass/*.css')
		.pipe(gulp.dest('public/stylesheets'));
});

// Sass
gulp.task('sass', function() {
    return gulp.src('public/sass/*.scss')
        .pipe(sass({ style: 'expanded', }))
	    .pipe(autoprefixer('last 2 version', 'safari 5', 'ie 8', 'ie 9', 'opera 12.1', 'ios 6', 'android 4'))
        .pipe(gulp.dest('public/stylesheets'))
    	.pipe(rename({ suffix: '.min' }))
	    .pipe(minifycss())
        .pipe(gulp.dest('public/stylesheets'));
});

// Concatenate & Minify JS
gulp.task('scripts', function() {
    return gulp.src('public/javascripts/*.js')
        .pipe(concat('app.js'))
        .pipe(gulp.dest('dist'))
        .pipe(rename('app.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest('dist'));
});

// Watch Files For Changes
gulp.task('watch', function() {
    gulp.watch('public/javascripts/*.js', ['lint', 'scripts']);
    gulp.watch('public/sass/*.css', ['css-copy']);
    gulp.watch('public/sass/*.scss', ['sass']);
});

// Default Task
gulp.task('default', ['lint', 'css-copy', 'sass', 'scripts', 'watch']);